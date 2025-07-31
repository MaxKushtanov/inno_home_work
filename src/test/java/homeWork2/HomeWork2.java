package homeWork2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HomeWork2 {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(5, 10, 9, 10, 13, 10);
        System.out.println("\nЗадание №1");
        find3rdBiggestNumber(list);
        System.out.println("\nЗадание №2");
        find3rdBiggestUniqueNumber(list);

        List<Employee> people = List.of(
                new Employee("Alice", "Engineer", 33),
                new Employee("Bob", "Director", 54),
                new Employee("Charlie", "Engineer", 24),
                new Employee("Diana", "Advisor", 37),
                new Employee("Eve", "Engineer", 42)
        );
        System.out.println("\nЗадание №3");
        getEngineersNames(people);
        System.out.println("\nЗадание №4");
        findAverageAge(people);

        List<String> words = List.of("aaa", "bbb", "hhhh", "w", "ccccccc");
        System.out.println("\nЗадание №5");
        findLongestWord(words);

        String someString = "fff ff ttt ggg sss rrr rrr ttt ttt ttttt dd ss";
        System.out.println("\nЗадание №6");
        createMapValueCount(someString);
        System.out.println("\nЗадание №7");
        printInLengthOrder(words);

        String[] multiString = new String[]{"fff ddd ss wewewr hghg", "sdsf ;ggdfg bnuhdfjg erer ee", "sdsd bbf sew wewg hfg"};
        System.out.println("\nЗадание №8");
        getLongestWordInArray(multiString);
    }

    public static void find3rdBiggestNumber(List<Integer> list) {
        Integer number = list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()).get(2);
        System.out.println("третье большое число = " + number);
    }

    public static void find3rdBiggestUniqueNumber(List<Integer> list) {
        Integer number = list.stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList()).get(2);
        System.out.println("третье большое уникальное число = " + number);
    }

    public static void getEngineersNames(List<Employee> list) {
        List<String> names = list.stream().filter(e -> e.getPosition().equals("Engineer"))
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .map(Employee::getName).collect(Collectors.toList());
        System.out.println("имена инженеров " + names);
    }

    public static void findAverageAge(List<Employee> list) {
        double number = list.stream().filter(e -> e.getPosition().equals("Engineer"))
                .mapToInt(Employee::getAge).average().orElseThrow(() -> new RuntimeException("Не удалось посчитать среднее"));
        System.out.println("средний возраст инженера " + number);
    }

    public static void findLongestWord(List<String> list) {
        String number = list.stream().sorted(Comparator.comparingInt(String::length).reversed()).collect(Collectors.toList()).get(0);
        System.out.println("самое большое слово = " + number);
    }

    public static void createMapValueCount(String someString) {
        Map<String, Long> map = Arrays.stream(someString.split(" "))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("мапа слово - количество вхождений = " + map);
    }

    public static void printInLengthOrder(List<String> list) {
        list.stream().sorted(Comparator.comparingInt(String::length).thenComparing(String::compareTo)).forEach(System.out::println);
    }

    public static void getLongestWordInArray(String[] multiString) {
        String longest = Arrays.stream(multiString).map(s -> Arrays.stream(s.split(" ")).max(Comparator.comparingInt(String::length)).get())
                .sorted(Comparator.comparingInt(String::length).reversed())
                .collect(Collectors.toList()).get(0);
        System.out.println(longest);
    }
}
