package com.github.johnsonmoon.calculate.chain.test2.calculator;

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;

/**
 * Create by xuyh at 2019/9/10 16:13.
 */
@Calculate(order = 1, contextType = String.class)
public class Calculator1 implements Calculator<String> {
    @Override
    public String doCalculate(String s) {
        System.out.println("String calculator " + Calculator1.class.getSimpleName() + " order 1");
        return s + "+";
    }
}
