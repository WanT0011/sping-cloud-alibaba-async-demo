package com.want.dubbo.consumer.aspect;

import com.want.dubbo.consumer.aspect.annotation.ExecTimeLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author WangZhiJian
 * @since 2021/2/1
 */
@Aspect
@Component
public class ExecTimeLogAspect {


    @Around("@annotation(execTimeLog)")
    public Object around(ProceedingJoinPoint pjp, ExecTimeLog execTimeLog){
        Signature signature = pjp.getSignature();
        Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());
        long start = System.currentTimeMillis();
        Object result = null;
        try{
            result = pjp.proceed();
        } catch (Throwable throwable) {
            logger.error("exception",throwable);
            throw new RuntimeException(throwable);
        }finally {
            logger.info("{} 执行花费 {}  毫秒",signature.getName(), (System.currentTimeMillis() - start));
        }
        return result;
    }

}
