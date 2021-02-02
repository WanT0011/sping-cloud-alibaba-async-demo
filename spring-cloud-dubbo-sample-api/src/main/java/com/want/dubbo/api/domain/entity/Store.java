package com.want.dubbo.api.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author WangZhiJian
 * @since 2021/1/29
 */
@Data
@NoArgsConstructor
public class Store implements Serializable {

    private Integer id;

    private List<Integer> productIdList;

    private List<Integer> discountIdList;


    public Store(Integer id) {
        this.id = id;
        productIdList = Arrays.asList(id,id+1,id+2,id+3);
        discountIdList = Arrays.asList(id,id+1);
    }
}
