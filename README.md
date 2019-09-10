# calculate-chain
Calculate chain framework

链式调用(计算)框架

# How to use (如何使用)
- Clone source code (克隆代码)

Make sure that **git** is installed. (确保git工具已经安装)
```
git clone https://github.com/johnsonmoon/calculate-chain.git
```

- Build source code (本地构建安装)

Make sure that **maven** is installed. (确保maven工具已经安装)
```
cd calculate-chain
mvn clean install -Dmaven.test.skip=true
```

- Import dependency into your project (在你的项目中引入依赖) 
```xml
<dependency>
    <groupId>com.github.johnsonmoon</groupId>
    <artifactId>calculate-chain</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

- Coding (编写代码)

# Examples (使用示例)

## Example 1
### Source code structure (代码结构)
```
|- src
    |- com.xxx.xxx.xxx
        |- calculator
            |- Calculator1.java
            |- Calculator2.java
            |- Calculator3.java
        |- Test.java
```

### Source code (代码)
```
# Calculator1.java

import com.github.johnsonmoon.calculate.chain.calculator.AbstractCalculator;

public class Calculator1 extends AbstractCalculator<Integer> {
    @Override
    public Class<Integer> contextType() {
        return Integer.class;
    }

    @Override
    public int order() {
        return 3;
    }

    @Override
    public Integer doCalculate(Integer integer) {
        System.out.println(contextType().getSimpleName() + " calculator " + Calculator1.class.getSimpleName() + " order " + order());
        return integer + 1;
    }
}

# Calculator2.java

import com.github.johnsonmoon.calculate.chain.calculator.AbstractCalculator;

public class Calculator2 extends AbstractCalculator<Integer> {
    @Override
    public Class<Integer> contextType() {
        return Integer.class;
    }

    @Override
    public int order() {
        return 2;
    }

    @Override
    public Integer doCalculate(Integer integer) {
        System.out.println(contextType().getSimpleName() + " calculator " + Calculator2.class.getSimpleName() + " order " + order());
        return integer + 1;
    }
}

# Calculator3.java

import com.github.johnsonmoon.calculate.chain.calculator.AbstractCalculator;

public class Calculator3 extends AbstractCalculator<Integer> {
    @Override
    public Class<Integer> contextType() {
        return Integer.class;
    }

    @Override
    public int order() {
        return 1;
    }

    @Override
    public Integer doCalculate(Integer integer) {
        System.out.println(contextType().getSimpleName() + " calculator " + Calculator3.class.getSimpleName() + " order " + order());
        return integer + 1;
    }
}

# Test.java

public class Test {
    public static void main(String[] args) {
        System.out.println(CalculatorChain.doCalculate(0));
    }
}

```

### The results (运行结果)

Run Test#main, the result is:

```
Integer calculator Calculator3 order 1
Integer calculator Calculator2 order 2
Integer calculator Calculator1 order 3
3
```

## Example 2
### Source code structure
```
|- src
 |- com.xxx.xxx.xxx
     |- calculator
         |- Calculator1.java
         |- Calculator2.java
         |- Calculator3.java
     |- Test.java
```

### Source code
```
# Calculator1.java

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;

@Calculate(order = 1, contextType = String.class)
public class Calculator1 implements Calculator<String> {
    @Override
    public String doCalculate(String s) {
        System.out.println("String calculator " + Calculator1.class.getSimpleName() + " order 1");
        return s + "+";
    }
}


# Calculator2.java

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;

@Calculate(order = 2, contextType = String.class)
public class Calculator2 implements Calculator<String> {
    @Override
    public String doCalculate(String s) {
        System.out.println("String calculator " + Calculator2.class.getSimpleName() + " order 2");
        return s + "+";
    }
}

# Calculator3.java

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;

@Calculate(order = 3, contextType = String.class)
public class Calculator3 implements Calculator<String> {
    @Override
    public String doCalculate(String s) {
        System.out.println("String calculator " + Calculator3.class.getSimpleName() + " order 3");
        return s + "+";
    }
}

# Test.java

public class Test {
    public static void main(String[] args) {
        System.out.println(CalculatorChain.doCalculate("Hello "));
    }
}

```

### The results

Run Test#main, the result is:

```
String calculator Calculator1 order 1
String calculator Calculator2 order 2
String calculator Calculator3 order 3
Hello +++
```


## Example 3
### Source code structure
```
|- src
 |- com.xxx.xxx.xxx
     |- calculator
        |- Calculator1.java
        |- Calculator2.java
        |- Calculator3.java
     |- context
        |- Context.java
     |- Test.java
```
### Source code
```
# Context.java

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

# Calculator1.java

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;
import com.github.johnsonmoon.calculate.chain.test3.context.Context;

@Calculate(order = 3, contextType = Context.class)
public class Calculator1 implements Calculator<Context> {
    @Override
    public Context doCalculate(Context context) {
        System.out.println("Context calculator " + Calculator1.class.getSimpleName() + " order 3");
        Integer in = context.getParamIn() == null ? 0 : context.getParamIn();
        Integer out = context.getParamOut() == null ? 0 : context.getParamOut();
        context.setParamOut(out + in);
        return context;
    }
}

# Calculator2.java

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;
import com.github.johnsonmoon.calculate.chain.test3.context.Context;

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

# Calculator3.java

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;
import com.github.johnsonmoon.calculate.chain.test3.context.Context;

@Calculate(order = 1, contextType = Context.class)
public class Calculator3 implements Calculator<Context> {
    @Override
    public Context doCalculate(Context context) {
        System.out.println("Context calculator " + Calculator3.class.getSimpleName() + " order 1");
        Integer in = context.getParamIn() == null ? 0 : context.getParamIn();
        Integer out = context.getParamOut() == null ? 0 : context.getParamOut();
        context.setParamOut(out + in);
        return context;
    }
}


# Test.java

public class Test {
    public static void main(String[] args) {
        Context context = new Context();
        context.setParamIn(1);
        System.out.println(CalculatorChain.doCalculate(context));
    }
}

```

### The results

Run Test#main, the result is:

```
Context calculator Calculator3 order 1
Context calculator Calculator2 order 2
Context calculator Calculator1 order 3
Context{paramIn=1, paramOut=3}
```


## Example 4
### Source code structure
```
|- src
 |- com.xxx.xxx.xxx
     |- calculator
        |- Calculator1.java
        |- Calculator2.java
        |- Calculator3.java
        |- Calculator4.java
     |- context
        |- MixContext.java
     |- Test.java
```
### Source code
```
# MixContext.java

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

# Calculator1.java

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;
import com.github.johnsonmoon.calculate.chain.test4.context.MixContext;

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

# Calculator2.java

import com.github.johnsonmoon.calculate.chain.calculator.AbstractCalculator;
import com.github.johnsonmoon.calculate.chain.test4.context.MixContext;

public class Calculator2 extends AbstractCalculator<MixContext> {
    @Override
    public Class<MixContext> contextType() {
        return MixContext.class;
    }

    @Override
    public int order() {
        return 3;
    }

    @Override
    public MixContext doCalculate(MixContext mixContext) {
        System.out.println(contextType().getSimpleName() + " calculator " + Calculator2.class.getSimpleName() + " order " + order());
        String append = mixContext.getAppend() == null ? "+" : mixContext.getAppend();
        String paramOut = mixContext.getParamOut() == null ? "" : mixContext.getParamOut();
        mixContext.setParamOut(paramOut + append);
        return mixContext;
    }
}

# Calculator3.java

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;
import com.github.johnsonmoon.calculate.chain.test4.context.MixContext;

@Calculate(order = 2, contextType = MixContext.class)
public class Calculator3 implements Calculator<MixContext> {
    @Override
    public MixContext doCalculate(MixContext mixContext) {
        System.out.println("MixContext calculator " + Calculator3.class.getSimpleName() + " order 2");
        String append = mixContext.getAppend() == null ? "+" : mixContext.getAppend();
        String paramOut = mixContext.getParamOut() == null ? "" : mixContext.getParamOut();
        mixContext.setParamOut(paramOut + append);
        return mixContext;
    }
}

# Calculator4.java

import com.github.johnsonmoon.calculate.chain.calculator.AbstractCalculator;
import com.github.johnsonmoon.calculate.chain.test4.context.MixContext;

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

# Test.java

public class Test {
    public static void main(String[] args) {
        MixContext context = new MixContext();
        context.setAppend("+");
        context.setParamOut("Hello! ");
        System.out.println(CalculatorChain.doCalculate(context));
    }
}

```

### The results

Run Test#main, the result is:

```
MixContext calculator Calculator4 order 1
MixContext calculator Calculator3 order 2
MixContext calculator Calculator2 order 3
MixContext calculator Calculator1 order 4
MixContext{append='+', paramOut='Hello! ++++'}
```