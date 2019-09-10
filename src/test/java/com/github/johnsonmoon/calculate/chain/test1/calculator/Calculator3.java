package com.github.johnsonmoon.calculate.chain.test1.calculator;

import com.github.johnsonmoon.calculate.chain.calculator.AbstractCalculator;

/**
 * Create by xuyh at 2019/9/10 16:09.
 */
public class Calculator3 extends AbstractCalculator<Integer> {
    @Override
    public Class<Integer> contextType() {
        return Integer.class;
    }

    @Override
    public int order() {
        return 1;
    }

    @Override
    public Integer doCalculate(Integer integer) {
        System.out.println(contextType().getSimpleName() + " calculator " + Calculator3.class.getSimpleName() + " order " + order());
        return integer + 1;
    }
}
