package com.github.johnsonmoon.calculate.chain.calculator;

/**
 * Create by xuyh at 2019/9/10 15:29.
 */
public abstract class AbstractCalculator<CONTEXT> implements Calculator<CONTEXT> {
    /**
     * Get the calculate context type.
     */
    public abstract Class<CONTEXT> contextType();

    /**
     * The order for calculating, from small to large
     */
    public abstract int order();
}
