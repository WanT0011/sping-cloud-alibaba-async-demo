package com.want.dubbo.api.service;

import com.want.dubbo.api.domain.entity.Product;
import com.want.dubbo.api.domain.entity.Store;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author WangZhiJian
 * @since 2021/1/29
 */
public interface StoreService {

    /**
     * 获取商店列表
     * @return
     */
    List<Store> list();

    /**
     * 根据商店id获取商品的列表
     * @param storeId
     * @return
     */
    List<Product> listProduct(Integer storeId);

    /**
     * 获取商店信息
     * @return
     */
    Store listById(Integer storeId);

    /**
     * 获取商品信息
     * @return
     */
    Product listProductById(Integer productId);

    /**
     * async获取商品信息
     * @return
     */
    default CompletableFuture<Product> listProductById(Integer productId,Boolean sync){
        return CompletableFuture.completedFuture(listProductById(productId));
    }

    /**
     * async获取商店信息
     * @return
     */
    default CompletableFuture<Store> listById(Integer storeId,Boolean sync){
        return CompletableFuture.completedFuture(listById(storeId));
    }

    /**
     * async获取商店列表
     * @return
     */
    default CompletableFuture<List<Store>> list(Boolean sync){
        return CompletableFuture.completedFuture(list());
    }

    /**
     * async根据商店id获取商品的列表
     * @param storeId
     * @return
     */
    default CompletableFuture<List<Product>> listProduct(Integer storeId,Boolean sync){
        return CompletableFuture.completedFuture(listProduct(storeId));
    }

}
