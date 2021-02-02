package com.want.dubbo.api.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author WangZhiJian
 * @since 2021/1/29
 */
@Data
@NoArgsConstructor
public class Product implements Serializable {

    private Integer id;

    private Integer storeId;

    private String name;

    private Long price;

    private List<Integer> discountIdList;

    public Product(Integer id) {
        this.id = id;
        name = "商品-" + id;
        Random random = new Random();
        price = random.nextInt(100) + 50L;
        discountIdList = Arrays.asList(id,id + 1);
        storeId = id / 4 + 1;

    }
}
