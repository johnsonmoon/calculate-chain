package com.github.johnsonmoon.calculate.chain.test4.context;

/**
 * Create by xuyh at 2019/9/10 16:30.
 */
public class MixContext {
    private String append;
    private String paramOut;

    public String getAppend() {
        return append;
    }

    public void setAppend(String append) {
        this.append = append;
    }

    public String getParamOut() {
        return paramOut;
    }

    public void setParamOut(String paramOut) {
        this.paramOut = paramOut;
    }

    @Override
    public String toString() {
        return "MixContext{" +
                "append='" + append + '\'' +
                ", paramOut='" + paramOut + '\'' +
                '}';
    }
}
