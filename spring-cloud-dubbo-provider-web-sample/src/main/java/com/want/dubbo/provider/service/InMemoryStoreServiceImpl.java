package com.want.dubbo.provider.service;

import com.want.dubbo.api.domain.entity.Product;
import com.want.dubbo.api.domain.entity.Store;
import com.want.dubbo.api.service.StoreService;
import com.want.dubbo.provider.util.SleepUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author WangZhiJian
 * @since 2021/1/29
 */
@DubboService(version = "1.0.0",protocol = "dubbo")
public class InMemoryStoreServiceImpl implements StoreService, SmartInitializingSingleton {

    private Map<Integer, Store> storeRepository;


    private Map<Integer,List<Product>> productRepository;


    /**
     * 获取商店列表
     *
     * @return
     */
    @Override
    public List<Store> list() {
        SleepUtil.sleepLessOneSecond();
        return new ArrayList<>(storeRepository.values());
    }

    /**
     * 根据商店id获取商品的列表
     *
     * @param storeId
     * @return
     */
    @Override
    public List<Product> listProduct(Integer storeId) {
        SleepUtil.sleepLessOneSecond();
        return productRepository.get(storeId);
    }

    /**
     * 获取商店信息
     *
     * @param storeId
     * @return
     */
    @Override
    public Store listById(Integer storeId) {
        SleepUtil.sleepLessOneSecond();
        return storeRepository.get(storeId);
    }

    /**
     * 获取商品信息
     *
     * @param productId
     * @return
     */
    @Override
    public Product listProductById(Integer productId) {
        SleepUtil.sleepLessOneSecond();
        return productRepository.values().stream()
                .flatMap(Collection::stream)
                .filter(p -> Objects.equals(productId,p.getId()))
                .reduce((v1,v2) -> v1)
                .orElse(null);
    }

    /**
     * 初始化 4个商店
     * 每个商店4个商品，每个商品2个折扣
     */
    @Override
    public void afterSingletonsInstantiated() {
        int storeCount = 4;

        storeRepository = Stream.iterate(1, n -> n+1)
                .limit(storeCount)
                .map(Store::new)
                .collect(Collectors.toMap(Store::getId, Function.identity(),(v1, v2) -> v1,() -> new ConcurrentHashMap<>(16)));

        productRepository = Stream.iterate(1, n -> n+1)
                .limit(storeCount * 4)
                .map(Product::new)
                .collect(Collectors.groupingBy(Product::getStoreId,() -> new ConcurrentHashMap<>(8),toList()));
    }
}
