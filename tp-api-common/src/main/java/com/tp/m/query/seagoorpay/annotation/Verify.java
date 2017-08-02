package com.tp.m.query.seagoorpay.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ldr on 2016/11/26.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Verify {

     boolean nullable() default false;

     int maxLength() default 0;

     boolean isInt() default false;

    String combine() default "";
}
