package com.want.dubbo.provider.util;

import lombok.SneakyThrows;

/**
 * @author WangZhiJian
 * @since 2021/1/29
 */
public class SleepUtil {


    /**
     * 睡 一半~全量的 millis
     * @param millis
     */
    @SneakyThrows
    public static void randomSleep(int millis){
//        Random random = new Random();
//        millis = random.nextInt(millis/2) + millis/2;
        Thread.sleep(millis);
    }

    /**
     * 睡 500 ~ 1000 毫秒
     */
    public static void sleepLessOneSecond(){
        randomSleep(500);
    }
}
