package com.oliver.eshop.h2;

import com.oliver.eshop.service.exception.OptimisticLockingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DatabaseExceptionAspect {

    @Around("execution(* com.oliver.eshop.h2.product.H2WriteProductRepository.*(..)) " +
            "|| execution(* com.oliver.eshop.h2.order.H2WriteOrderRepository.*(..))")
    public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new OptimisticLockingException("Optimistic locking exception occurred");
        }
    }
}
