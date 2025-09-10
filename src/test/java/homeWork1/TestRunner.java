package homeWork1;

import homeWork1.annotations.AfterSuite;
import homeWork1.annotations.BeforeSuite;
import homeWork1.annotations.CsvSource;
import homeWork1.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRunner {

    public static void run(Class<?> clazz) throws Exception {
        List<Method> tests = new ArrayList<>();
        Method beforeSuite = null;
        Method afterSuite = null;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuite != null) {
                    throw new RuntimeException("класс содержит больше одного метода с аннотацией BeforeSuite!");
                }
                beforeSuite = method;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuite != null) {
                    throw new RuntimeException("класс содержит больше одного метода с аннотацией AfterSuite!");
                }
                afterSuite = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                if (method.getAnnotation(Test.class).priority() < 1 ||
                        method.getAnnotation(Test.class).priority() > 10) {
                    throw new RuntimeException("значение поля priority должно быть в диапозоне от 1 до 10");
                }
                tests.add(method);
            }
            tests.sort(Comparator.comparingInt((Method m) ->
                    m.getAnnotation(Test.class).priority()).reversed());
        }

        Object instance = clazz.getDeclaredConstructor().newInstance();
        if (beforeSuite != null) {
            beforeSuite.invoke(null);
        }
        for (Method m : tests) {
            if (m.isAnnotationPresent(CsvSource.class)) {
                String[] args = m.getAnnotation(CsvSource.class).value().split(",");
                if (args.length != 3) {
                    throw new RuntimeException("Строка должна содержать 3 аргумента int,String,boolean через запятую," +
                            "например 1,тест, true");
                }
                int first = Integer.parseInt(args[0]);
                String second = args[1];
                if (!(args[2].equals("true") || args[2].equals("false"))) {
                    throw new RuntimeException("третий аргумент принимает только аргументы true или false");
                }
                boolean third = Boolean.parseBoolean(args[2]);
                m.invoke(instance, first, second, third);
            } else {
                m.invoke(instance);
            }
        }
        if (afterSuite != null) {
            afterSuite.invoke(null);
        }
    }

}
