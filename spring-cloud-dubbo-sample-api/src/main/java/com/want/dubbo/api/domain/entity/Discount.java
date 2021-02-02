package com.want.dubbo.api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Random;

/**
 * @author WangZhiJian
 * @since 2021/1/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount implements Serializable {

    public static final Discount NO_DISCOUNT = new Discount(0,100,false);

    private Integer id;

    private int discount;

    private Boolean ttl;

    public Discount(Integer id) {
        this.id = id;
        Random random = new Random();
        discount = random.nextInt(40) + 60;
        ttl = false;
    }


}
