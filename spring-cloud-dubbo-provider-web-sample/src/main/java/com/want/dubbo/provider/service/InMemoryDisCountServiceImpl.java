package com.want.dubbo.provider.service;

import com.want.dubbo.api.domain.entity.Discount;
import com.want.dubbo.api.service.DisCountService;
import com.want.dubbo.provider.util.SleepUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author WangZhiJian
 * @since 2021/1/29
 */
@DubboService(version = "1.0.0",protocol = "dubbo")
public class InMemoryDisCountServiceImpl implements DisCountService, SmartInitializingSingleton {

    private Map<Integer, Discount> discountRepository;

    /**
     * 根据折扣id 查询折扣实体
     *
     * @param ids
     * @return
     */
    @Override
    public List<Discount> listById(List<Integer> ids) {
        SleepUtil.sleepLessOneSecond();
        return Optional.ofNullable(ids)
                .map(idList -> idList.stream().map(discountRepository::get).collect(toList()))
                .orElse(Collections.emptyList());
    }


    /**
     * 初始化 商品个数 * 2 的数量
     */
    @Override
    public void afterSingletonsInstantiated() {
        discountRepository = Stream.iterate(1, n -> n+1)
                .limit(4 * 4 * 2)
                .map(Discount::new)
                .collect(Collectors.toMap(Discount::getId, Function.identity(),(v1,v2) -> v1));
    }
}
