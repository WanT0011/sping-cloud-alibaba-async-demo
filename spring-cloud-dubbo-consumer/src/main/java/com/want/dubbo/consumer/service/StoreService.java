package com.want.dubbo.consumer.service;

import com.want.dubbo.api.domain.entity.Product;
import com.want.dubbo.api.domain.entity.Store;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.List;

/**
 * @author WangZhiJian
 * @since 2021/1/29
 */
@Slf4j
@DubboService(version = "2.0.0")
public class StoreService  implements com.want.dubbo.api.service.StoreService {

    @DubboReference(version = "1.0.0")
    private com.want.dubbo.api.service.StoreService storeService;

    /**
     * 获取商店列表
     *
     * @return
     */
    @Override
    public List<Store> list() {

        RpcContext context = RpcContext.getContext();

        context.setAttachment("demo","spring-cloud-alibaba-dubbo-consumer");

        List<Store> stores = storeService.list();

        String demo = context.getAttachment("demo");
        log.info(demo);
        return stores;
    }

    /**
     * 根据商店id获取商品的列表
     *
     * @param storeId
     * @return
     */
    @Override
    public List<Product> listProduct(Integer storeId) {
        return null;
    }

    /**
     * 获取商店信息
     *
     * @param storeId
     * @return
     */
    @Override
    public Store listById(Integer storeId) {
        return null;
    }

    /**
     * 获取商品信息
     *
     * @param productId
     * @return
     */
    @Override
    public Product listProductById(Integer productId) {
        return null;
    }
}
