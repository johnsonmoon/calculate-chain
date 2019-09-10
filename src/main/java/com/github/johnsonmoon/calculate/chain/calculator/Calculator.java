package com.github.johnsonmoon.calculate.chain.calculator;

/**
 * Calculate type template interface.
 * <p>
 * Create by xuyh at 2019/8/20 11:06.
 */
public interface Calculator<CONTEXT> {
    /**
     * Calculation operate.
     *
     * @param context Calculation context parameter.
     * @return Calculation context parameter.
     */
    CONTEXT doCalculate(CONTEXT context);
}
