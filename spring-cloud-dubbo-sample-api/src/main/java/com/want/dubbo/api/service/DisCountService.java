package com.want.dubbo.api.service;

import com.want.dubbo.api.domain.entity.Discount;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 折扣服务
 *
 * @author WangZhiJian
 * @since 2021/1/29
 */
public interface DisCountService {

    /**
     * 根据折扣id 查询折扣实体
     *
     * @param ids
     * @return
     */
    List<Discount> listById(List<Integer> ids);

    default CompletableFuture<List<Discount>> listById(List<Integer> ids, Boolean sync){
        return CompletableFuture.completedFuture(listById(ids));
    }
}
