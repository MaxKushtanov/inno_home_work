//package org.example;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//public class Main {
//    public static void main(String[] args) throws Exception {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//
//        DataSource dataSource = context.getBean(DataSource.class);
//
//        try (Connection conn = dataSource.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT 1")) {
//
//            if (rs.next()) {
//                int result = rs.getInt(1);
//                System.out.println("✅ БД ответила: " + result);
//            }
//        }
//
//        context.close();
//    }
//}

package homeWork4;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var userService = context.getBean(UserService.class);

        // Создание
        userService.registerUser("leo123");

        // Получение
        var users = userService.getAllUsers();
        users.forEach(System.out::println);

        // Переименование
        if (!users.isEmpty()) {
            var firstUser = users.get(0);
            userService.renameUser(firstUser.getId(), "leo456");
            System.out.println("✅ После переименования:");
            userService.getAllUsers().forEach(System.out::println);
        }

        // Удаление
        if (!users.isEmpty()) {
            var firstUser = users.get(0);
            userService.deleteUser(firstUser.getId());
            System.out.println("✅ После удаления:");
            userService.getAllUsers().forEach(System.out::println);
        }

        context.close();
    }
}