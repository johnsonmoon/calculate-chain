package com.github.johnsonmoon.calculate.chain.test3;

import com.github.johnsonmoon.calculate.chain.CalculatorChain;
import com.github.johnsonmoon.calculate.chain.test3.context.Context;

/**
 * Create by xuyh at 2019/9/10 16:18.
 */
public class Test {
    public static void main(String[] args) {
        Context context = new Context();
        context.setParamIn(1);
        System.out.println(CalculatorChain.doCalculate(context));
    }
}
