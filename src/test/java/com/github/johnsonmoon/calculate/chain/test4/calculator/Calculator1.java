package com.github.johnsonmoon.calculate.chain.test4.calculator;

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;
import com.github.johnsonmoon.calculate.chain.test4.context.MixContext;

/**
 * Create by xuyh at 2019/9/10 16:29.
 */
@Calculate(order = 4, contextType = MixContext.class)
public class Calculator1 implements Calculator<MixContext> {
    @Override
    public MixContext doCalculate(MixContext mixContext) {
        System.out.println("MixContext calculator " + Calculator1.class.getSimpleName() + " order 4");
        String append = mixContext.getAppend() == null ? "+" : mixContext.getAppend();
        String paramOut = mixContext.getParamOut() == null ? "" : mixContext.getParamOut();
        mixContext.setParamOut(paramOut + append);
        return mixContext;
    }
}
