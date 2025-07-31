package homeWork2;

public class Employee {

    String name;
    String position;
    Integer age;

    public Employee(String name, String position, Integer age) {
        this.name = name;
        this.position = position;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public Integer getAge() {
        return age;
    }
}
