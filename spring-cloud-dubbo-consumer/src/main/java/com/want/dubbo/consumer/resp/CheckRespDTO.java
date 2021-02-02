package com.want.dubbo.consumer.resp;

import com.want.dubbo.api.domain.entity.Discount;
import lombok.Data;

import java.io.Serializable;

/**
 * 校验的响应实体
 *
 * @author WangZhiJian
 * @since 2021/2/1
 */
@Data
public class CheckRespDTO implements Serializable {

    /**
     * 最优折扣
     */
    private Discount discount;

    /**
     * 商品原价格
     */
    private Long price;

    /**
     * 校验通过
     */
    private Boolean checkPass;
    
    

}
