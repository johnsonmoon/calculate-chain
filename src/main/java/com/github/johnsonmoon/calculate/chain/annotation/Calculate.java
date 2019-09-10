package com.github.johnsonmoon.calculate.chain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate the calculating type.
 * <p>
 * Create by xuyh at 2019/8/20 11:15.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Calculate {
    /**
     * The order for calculating, from small to large
     */
    int order() default 0;

    /**
     * Calculate context type.
     */
    Class<?> contextType();
}
