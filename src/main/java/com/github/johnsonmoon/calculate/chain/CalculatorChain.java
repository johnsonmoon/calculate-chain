package com.github.johnsonmoon.calculate.chain;

import com.github.johnsonmoon.calculate.chain.annotation.Calculate;
import com.github.johnsonmoon.calculate.chain.calculator.AbstractCalculator;
import com.github.johnsonmoon.calculate.chain.calculator.Calculator;
import com.github.johnsonmoon.calculate.chain.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Create by xuyh at 2019/8/20 10:59.
 */
@SuppressWarnings("all")
public class CalculatorChain {
    private static Logger logger = LoggerFactory.getLogger(CalculatorChain.class);

    private static Map<String, List<Calculator>> calculatorChainMapCache = new LinkedHashMap<>();

    static {
        String packageProp = System.getProperty("calculators.package");
        if (packageProp == null) {
            initialize("");
        } else {
            initialize(packageProp);
        }
    }

    private static void initialize(String packageName) {
        try {
            List<String> classNames;
            if (packageName != null && !packageName.equals("")) {
                classNames = ClassUtils.getAllClassNamesFromPackage(packageName);
            } else {
                classNames = ClassUtils.getAllClassNamesFromClassPath();
            }
            if (classNames == null) {
                return;
            }
            Map<String, List<Calculator>> calculatorChainMap = new HashMap<>();
            for (String className : classNames) {
                try {
                    if (className.startsWith("sun")
                            || className.startsWith("com.sun")
                            || className.startsWith("jdk")
                            || className.startsWith("java")
                            || className.startsWith("javax")
                            || className.startsWith("javafx")
                            || className.startsWith("oracle")
                            || className.startsWith("com.oracle")
                            || className.startsWith("apple")
                            || className.startsWith("com.apple")
                            || className.startsWith("netscape.security")
                            || className.startsWith("netscape.javascript")) {
                        continue;
                    }
                    if (className.equals(Calculator.class.getCanonicalName())
                            || className.equals(AbstractCalculator.class.getCanonicalName())) {
                        continue;
                    }
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(className);
                    } catch (Throwable e) {
                        logger.debug(e.getMessage());
                    }
                    if (clazz == null) {
                        continue;
                    }
                    add(clazz, calculatorChainMap);
                } catch (Exception e) {
                    logger.info(e.getMessage(), e);
                }
            }
            sortChain(calculatorChainMap);
            calculatorChainMap.forEach(calculatorChainMapCache::put);
        } catch (Exception e) {
            logger.info("Calculator dispatcher load failed, message: {}", e.getMessage());
        }
    }

    public static void initialize(Collection<Object> calculators) {
        if (calculators == null) {
            return;
        }
        try {
            Map<String, List<Calculator>> calculatorChainMap = new HashMap<>();
            for (Object calculator : calculators) {
                Class<?> clazz = calculator.getClass();
                add(clazz, calculatorChainMap);
            }
            sortChain(calculatorChainMap);
            calculatorChainMapCache.clear();
            calculatorChainMap.forEach(calculatorChainMapCache::put);
        } catch (Exception e) {
            logger.info("Calculator dispatcher load failed, message: {}", e.getMessage());
        }
    }

    private static void add(Class<?> clazz, Map<String, List<Calculator>> calculatorChainMap) {
        if (!ClassUtils.isImplemented(clazz, Calculator.class)) {
            return;
        }
        Object calculatorObj = null;
        try {
            calculatorObj = clazz.newInstance();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        if (calculatorObj instanceof AbstractCalculator) {
            AbstractCalculator abstractCalculator = (AbstractCalculator) calculatorObj;
            add(abstractCalculator.contextType(), abstractCalculator, calculatorChainMap);
            return;
        }
        if (calculatorObj instanceof Calculator) {
            if (!clazz.isAnnotationPresent(Calculate.class)) {
                return;
            }
            Calculate calculate = clazz.getDeclaredAnnotation(Calculate.class);
            Calculator calculator = (Calculator) calculatorObj;
            add(calculate.contextType(), calculator, calculatorChainMap);
        }
    }

    private static void add(Class<?> contextType, Calculator calculator, Map<String, List<Calculator>> calculatorChainMap) {
        if (contextType == null || calculator == null || calculatorChainMap == null) {
            return;
        }
        String key = contextType.getCanonicalName();
        List<Calculator> calculators;
        if (calculatorChainMap.get(key) != null && !calculatorChainMap.get(key).isEmpty()) {
            calculators = calculatorChainMap.get(key);
        } else {
            calculators = new ArrayList<>();
            calculatorChainMap.put(key, calculators);
        }
        calculators.add(calculator);
    }

    private static void sortChain(Map<String, List<Calculator>> calculatorChainMap) {
        for (Map.Entry<String, List<Calculator>> entry : calculatorChainMap.entrySet()) {
            String key = entry.getKey();
            List<Calculator> calculators = entry.getValue();
            calculators.sort((a, b) -> {
                int orderA = 0;
                int orderB = 0;
                try {
                    if (a.getClass().isAnnotationPresent(Calculate.class)) {
                        orderA = a.getClass().getDeclaredAnnotation(Calculate.class).order();
                    } else {
                        if (a instanceof AbstractCalculator) {
                            orderA = ((AbstractCalculator) a).order();
                        }
                    }
                    if (b.getClass().isAnnotationPresent(Calculate.class)) {
                        orderB = b.getClass().getDeclaredAnnotation(Calculate.class).order();
                    } else {
                        if (b instanceof AbstractCalculator) {
                            orderB = ((AbstractCalculator) b).order();
                        }
                    }
                } catch (Exception e) {
                    logger.debug(e.getMessage(), e);
                }
                return orderA - orderB;
            });
        }
    }

    /**
     * Looking for a corresponding chain of calculators and then do calculate.
     *
     * @param context Context for calculating.
     * @return Calculation result Context.
     */
    public static <CONTEXT> CONTEXT doCalculate(CONTEXT context) {
        String contextKey = context.getClass().getCanonicalName();
        List<Calculator> chain = calculatorChainMapCache.get(contextKey);
        if (chain != null && !chain.isEmpty()) {
            for (Calculator calculator : chain) {
                try {
                    context = (CONTEXT) calculator.doCalculate(context);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return context;
    }
}
