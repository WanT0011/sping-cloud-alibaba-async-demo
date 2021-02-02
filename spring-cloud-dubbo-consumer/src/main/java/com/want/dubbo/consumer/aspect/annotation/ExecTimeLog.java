package com.want.dubbo.consumer.aspect.annotation;

import java.lang.annotation.*;

/**
 * 执行时间日志打印
 * 不传递，只作用于当前方法
 *
 * @author WangZhiJian
 * @since 2021/2/1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  ExecTimeLog {
}
