package com.github.johnsonmoon.calculate.chain.test1;

import com.github.johnsonmoon.calculate.chain.CalculatorChain;
import com.github.johnsonmoon.calculate.chain.test1.calculator.Calculator1;
import com.github.johnsonmoon.calculate.chain.test1.calculator.Calculator2;
import com.github.johnsonmoon.calculate.chain.test1.calculator.Calculator3;

import java.util.Arrays;

/**
 * Create by xuyh at 2019/9/10 16:08.
 */
public class Test {
    public static void main(String[] args) {
        CalculatorChain.initialize(Arrays.asList(new Calculator1(), new Calculator2(), new Calculator3()));
        System.out.println(CalculatorChain.doCalculate(0));
    }
}
