package com.github.johnsonmoon.calculate.chain.test4;

import com.github.johnsonmoon.calculate.chain.CalculatorChain;
import com.github.johnsonmoon.calculate.chain.test4.context.MixContext;

/**
 * Create by xuyh at 2019/9/10 16:29.
 */
public class Test {
    public static void main(String[] args) {
        MixContext context = new MixContext();
        context.setAppend("+");
        context.setParamOut("Hello! ");
        System.out.println(CalculatorChain.doCalculate(context));
    }
}
