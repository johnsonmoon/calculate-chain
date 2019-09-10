package com.github.johnsonmoon.calculate.chain.test3.calculator;

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;
import com.github.johnsonmoon.calculate.chain.test3.context.Context;

/**
 * Create by xuyh at 2019/9/10 16:19.
 */
@Calculate(order = 2, contextType = Context.class)
public class Calculator2 implements Calculator<Context> {
    @Override
    public Context doCalculate(Context context) {
        System.out.println("Context calculator " + Calculator2.class.getSimpleName() + " order 2");
        Integer in = context.getParamIn() == null ? 0 : context.getParamIn();
        Integer out = context.getParamOut() == null ? 0 : context.getParamOut();
        context.setParamOut(out + in);
        return context;
    }
}
