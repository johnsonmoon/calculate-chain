package com.github.johnsonmoon.calculate.chain.test4.calculator;

import com.github.johnsonmoon.calculate.chain.calculator.AbstractCalculator;
import com.github.johnsonmoon.calculate.chain.test4.context.MixContext;

/**
 * Create by xuyh at 2019/9/10 16:29.
 */
public class Calculator4 extends AbstractCalculator<MixContext> {
    @Override
    public Class<MixContext> contextType() {
        return MixContext.class;
    }

    @Override
    public int order() {
        return 1;
    }

    @Override
    public MixContext doCalculate(MixContext mixContext) {
        System.out.println(contextType().getSimpleName() + " calculator " + Calculator4.class.getSimpleName() + " order " + order());
        String append = mixContext.getAppend() == null ? "+" : mixContext.getAppend();
        String paramOut = mixContext.getParamOut() == null ? "" : mixContext.getParamOut();
        mixContext.setParamOut(paramOut + append);
        return mixContext;
    }
}
