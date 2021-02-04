package com.want.dubbo.consumer.controller;

import com.want.dubbo.api.domain.entity.Discount;
import com.want.dubbo.api.domain.entity.Product;
import com.want.dubbo.api.domain.entity.Store;
import com.want.dubbo.api.domain.entity.User;
import com.want.dubbo.api.service.DisCountService;
import com.want.dubbo.api.service.StoreService;
import com.want.dubbo.api.service.UserService;
import com.want.dubbo.consumer.aspect.annotation.ExecTimeLog;
import com.want.dubbo.consumer.resp.CheckRespDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.aop.framework.AopContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 店铺id范围：
 *  1~4
 * 商品id范围：
 * 1~16
 * 折扣id范围：
 *
 *
 *
 * 模拟一个检查用户账户余额的操作
 *  1： 获取商店的折扣信息
 *  2： 获取商品的价格和折扣信息
 *  3： 获取用户账户余额
 *  4： 选用最低折扣并计算商品真实价格
 *  5： 和用户的账户余额做对比
 *
 * @author WangZhiJian
 * @since 2021/2/1
 */
@Slf4j
@Api(tags = "模拟检查用户账户余额的操作")
@RestController
@RequestMapping
public class BuyController {

    @DubboReference(version = "1.0.0")
    private StoreService storeService;

    @DubboReference(version = "1.0.0")
    private UserService userService;

    @DubboReference(version = "1.0.0")
    private DisCountService disCountService;

    /**
     *  使用dubbo异步的方式
     * @param uid 用户id
     * @param pid 商品id
     * @param sid 商店id
     * @return
     */
    @ApiOperation(value = "使用异步做调用")
    @ExecTimeLog
    @GetMapping("/async/checkUserAccountPrice/{uid}/{pid}/{sid}")
    public CheckRespDTO asyncCheckUserAccountPrice(@PathVariable("uid") Integer uid
            ,@PathVariable("pid") Integer pid,@PathVariable("sid") Integer sid) throws Exception {

        CheckRespDTO checkRespDTO = new CheckRespDTO();

        CompletableFuture<CheckRespDTO> future =
                // 根据商店id获取商店信息
                storeService.listById(sid, false)
                .thenCombine(
                        //同时我们再去获取商品信息
                        storeService.listProductById(pid, false)
                        // 将获取的商店和商品结果整合
                        , (store, product) -> {
                            checkRespDTO.setPrice(product.getPrice());
                            // 将商店和商品的 折扣 id整合
                            List<Integer> discountIds = Optional.ofNullable(store)
                                    .map(Store::getDiscountIdList)
                                    .map(list -> {
                                        list.addAll(Optional.ofNullable(product).map(Product::getDiscountIdList).orElse(Collections.emptyList()));
                                        return list;
                                    })
                                    .orElseGet(() -> Optional.of(product).map(Product::getDiscountIdList).orElse(null));
                            // 返回折扣服务进行查询所有的折扣信息
                            return disCountService.listById(discountIds);
                        })
                .thenCombine(
                        // 查询用户信息用户
                        userService.info(uid, false)
                        // 折扣信息和用户信息都查询到了，进行整合
                        , (discountList, user) -> {
                            // 选出最优折扣
                            Discount discount = Optional.ofNullable(discountList)
                                    .map(discounts -> discounts
                                            .stream()
                                            .filter(discount1 -> !discount1.getTtl())
                                            .reduce((v1, v2) -> {
                                                if (v1.getDiscount() > v2.getDiscount()) {
                                                    return v2;
                                                } else {
                                                    return v1;
                                                }
                                            }).orElse(null))
                                    .orElse(Discount.NO_DISCOUNT);

                            // 计算金额
                            Long price = discount.getDiscount() * checkRespDTO.getPrice();

                            //初始化返回值
                            checkRespDTO.setCheckPass(price < user.getMoney());
                            checkRespDTO.setDiscount(discount);
                            return checkRespDTO;
                        });

        return future.get(3000, TimeUnit.MILLISECONDS);
    }
    /**
     *  使用mono异步的方式
     * @param uid 用户id
     * @param pid 商品id
     * @param sid 商店id
     * @return
     */
    @ApiOperation(value = "使用mono异步做调用")
    @ExecTimeLog
    @GetMapping("/async/mono/checkUserAccountPrice/{uid}/{pid}/{sid}")
    public CheckRespDTO asyncByReactorCheckUserAccountPrice(@PathVariable("uid") Integer uid
            ,@PathVariable("pid") Integer pid,@PathVariable("sid") Integer sid) {

        CheckRespDTO checkRespDTO = new CheckRespDTO();

        Tuple2<Discount, User> res = Mono.zip(
                Mono.zip(
                        // 获取店铺信息
                        Mono.fromFuture(Optional.ofNullable(storeService.listById(sid, false)).orElseGet(()
                                -> storeService.listById(1, false)))
                        // 获取商品信息
                        , Mono.fromFuture(Optional.ofNullable(storeService.listProductById(pid, false)).orElseGet(()
                                -> storeService.listProductById(1, false)))
                        // 选出最优的折扣
                        , (store, product) -> {
                            checkRespDTO.setPrice(product.getPrice());
                            // 获取所有的折扣，将店铺和商品的折扣整合
                            List<Integer> discountIds = Optional.ofNullable(store)
                                    .map(Store::getDiscountIdList)
                                    .map(list -> {
                                        list.addAll(Optional.ofNullable(product)
                                                .map(Product::getDiscountIdList)
                                                .orElse(Collections.emptyList()));
                                        return list;
                                    })
                                    .orElseGet(() -> Optional.ofNullable(product)
                                            .map(Product::getDiscountIdList)
                                            .orElse(null));
                            // 根据id调用接口获取折扣选出最优
                            return Optional.ofNullable(discountIds)
                                    .map(disCountService::listById)
                                    .map(discounts -> discounts
                                            .stream()
                                            .filter(discount1 -> !discount1.getTtl())
                                            .reduce((v1, v2) -> {
                                                if (v2.getDiscount() >= v1.getDiscount()) {
                                                    return v1;
                                                } else {
                                                    return v2;
                                                }
                                            }).orElse(null))
                                    .orElse(Discount.NO_DISCOUNT);
                        })
                // 获取用户信息
                , Mono.fromFuture(userService.info(uid, false))
                //等待响应
        ).block(Duration.ofSeconds(3));
        /** 设置返回值 */
        checkRespDTO.setDiscount(res.getT1());
        checkRespDTO.setCheckPass(checkRespDTO.getPrice() * res.getT1().getDiscount() < res.getT2().getMoney());

        return checkRespDTO;
    }

    /**
     *  使用同步做调用
     * @param uid 用户id
     * @param pid 商品id
     * @param sid 商店id
     * @return
     */
    @ApiOperation(value = "使用同步做调用")
    @ExecTimeLog
    @GetMapping("/sync/checkUserAccountPrice/{uid}/{pid}/{sid}")
    public CheckRespDTO syncCheckUserAccountPrice(@PathVariable("uid") Integer uid
            ,@PathVariable("pid") Integer pid,@PathVariable("sid") Integer sid){
        // 查询店铺的信息,找不到就拿第一个商店
        Store store = Optional.ofNullable(storeService.listById(sid))
                .orElseGet(() -> storeService.listById(1));
        // 获取商品信息 ,找不到就拿第一个商品信息
        Product product = Optional.ofNullable(storeService.listProductById(pid))
                .orElseGet(() -> storeService.listProductById(1));
        // 获取所有的折扣
        List<Integer> discountIds = Optional.ofNullable(store)
                .map(Store::getDiscountIdList)
                .map(list -> {
                    list.addAll(Optional.ofNullable(product).map(Product::getDiscountIdList).orElse(Collections.emptyList()));
                    return list;
                })
                .orElseGet(() -> Optional.of(product).map(Product::getDiscountIdList).orElse(null));
        // 找出最优折扣，没有折扣就使用 Discount.NO_DISCOUNT
        Discount discount = Optional.ofNullable(discountIds)
                .map(disCountService::listById)
                .map(discounts -> discounts
                        .stream()
                        .filter(discount1 -> !discount1.getTtl())
                        .reduce((v1, v2) -> {
                            if (v1.getDiscount() > v2.getDiscount()) {
                                return v2;
                            } else {
                                return v1;
                            }
                        }).orElse(null))
                .orElse(Discount.NO_DISCOUNT);
        // 计算金额
        Long price = discount.getDiscount() * product.getPrice();
        // 获取用户账户余额
        User info = userService.info(uid);

        /** 设置返回值 */
        CheckRespDTO checkRespDTO = new CheckRespDTO();
        checkRespDTO.setCheckPass(price < info.getMoney());
        checkRespDTO.setDiscount(discount);
        checkRespDTO.setPrice(product.getPrice());
        return checkRespDTO;
    }









    @ApiOperation(value = "使用异步做调用n次")
    @GetMapping(value = "/asyncMoreCount/{count}")
    public void asyncMoreCount(@PathVariable("count") Integer count) throws Exception {
        BuyController buyController = (BuyController)AopContext.currentProxy();

        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            log.info("{} times",i+1);
            buyController.asyncCheckUserAccountPrice(1,1,1);
        }
        long end = System.currentTimeMillis();
        log.info("异步总共花费 {}，平均每次 {} ",end - start,(end - start)/count);
    }

    @ApiOperation(value = "使用同步做调用n次")
    @GetMapping(value = "/syncMoreCount/{count}")
    public void syncMoreCount(@PathVariable("count") Integer count){
        BuyController buyController = (BuyController)AopContext.currentProxy();

        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            log.info("{} times",i+1);
            buyController.syncCheckUserAccountPrice(1,1,1);
        }
        long end = System.currentTimeMillis();
        log.info("同步总共花费 {}，平均每次 {} ",end - start,(end - start)/count);
    }

    @ApiOperation(value = "使用Mono做调用n次")
    @GetMapping(value = "/asyncMonoMoreCount/{count}")
    public void asyncMonoMoreCount(@PathVariable("count") Integer count) {
        BuyController buyController = (BuyController)AopContext.currentProxy();

        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            log.info("{} times",i+1);
            buyController.asyncByReactorCheckUserAccountPrice(1,1,1);
        }
        long end = System.currentTimeMillis();
        log.info("异步总共花费 {}，平均每次 {} ",end - start,(end - start)/count);
    }
}
