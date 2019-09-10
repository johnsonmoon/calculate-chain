package com.github.johnsonmoon.calculate.chain.test3.context;

/**
 * Create by xuyh at 2019/9/10 16:22.
 */
public class Context {
    private Integer paramIn;
    private Integer paramOut;

    public Integer getParamIn() {
        return paramIn;
    }

    public void setParamIn(Integer paramIn) {
        this.paramIn = paramIn;
    }

    public Integer getParamOut() {
        return paramOut;
    }

    public void setParamOut(Integer paramOut) {
        this.paramOut = paramOut;
    }

    @Override
    public String toString() {
        return "Context{" +
                "paramIn=" + paramIn +
                ", paramOut=" + paramOut +
                '}';
    }
}
