package com.github.johnsonmoon.calculate.chain.test2.calculator;

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;

/**
 * Create by xuyh at 2019/9/10 16:13.
 */
@Calculate(order = 2, contextType = String.class)
public class Calculator2 implements Calculator<String> {
    @Override
    public String doCalculate(String s) {
        System.out.println("String calculator " + Calculator2.class.getSimpleName() + " order 2");
        return s + "+";
    }
}
