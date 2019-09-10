package com.github.johnsonmoon.calculate.chain.test2.calculator;

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;

/**
 * Create by xuyh at 2019/9/10 16:13.
 */
@Calculate(order = 3, contextType = String.class)
public class Calculator3 implements Calculator<String> {
    @Override
    public String doCalculate(String s) {
        System.out.println("String calculator " + Calculator3.class.getSimpleName() + " order 3");
        return s + "+";
    }
}
