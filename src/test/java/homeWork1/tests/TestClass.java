package homeWork1.tests;

import homeWork1.annotations.AfterSuite;
import homeWork1.annotations.BeforeSuite;
import homeWork1.annotations.CsvSource;
import homeWork1.annotations.Test;

public class TestClass {

    @BeforeSuite
    public static void before() {
        System.out.println("метод BeforeSuite вызван!");
    }

//    @BeforeSuite
//    public void beforeWithFail() {
//        System.out.println("метод BeforeSuite вызван!");
//    }

    @AfterSuite
    public static void after() {
        System.out.println("метод AfterSuite вызван!");
    }

//    @AfterSuite
//    public static void afterWithFail() {
//        System.out.println("метод AfterSuite вызван!");
//    }

    @Test(priority = 1)
    public void testMethod1() {
        System.out.println("тест с приоритетом 1 вызван");
    }

    @Test(priority = 10)
    public void testMethod10() {
        System.out.println("тест с приоритетом 10 вызван");
    }

//    @Test(priority = 0)
//    public void testMethod0() {
//        System.out.println("тест с приоритетом 10 вызван");
//    }

//    @Test(priority = 11)
//    public void testMethod11() {
//        System.out.println("тест с приоритетом 10 вызван");
//    }

    @Test()
    public void testMethodDefault() {
        System.out.println("тест с дефолтным(5) приоритетом вызван");
    }

    @Test(priority = 4)
    public void testMethod4() {
        System.out.println("тест с приоритетом 4 вызван");
    }

    @Test(priority = 6)
    public void testMethod6() {
        System.out.println("тест с приоритетом 6 вызван");
    }

    @Test(priority = 7)
    @CsvSource("1,бла-бла,true")
    public void testMethodWithArgs(int a, String b, boolean c) {
        System.out.println("тест с приоритетом 7 вызван");
        System.out.println("int = " + a);
        System.out.println("string = " + b);
        System.out.println("boolean = " + c);
    }

//    @Test(priority = 8)
//    @CsvSource("1,бла-бла,dfdfd")
//    public void testMethodWithArgsWithFail(int a, String b, boolean c) {
//        System.out.println("тест с приоритетом 8 вызван");
//        System.out.println("int = " + a);
//        System.out.println("string = " + b);
//        System.out.println("boolean = " + c);
//    }

}
