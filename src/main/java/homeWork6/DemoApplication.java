package homeWork6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "homeWork6", // продукты/репозитории сем. 11-12 (контроллеры и т.д.)
        }
)
@EnableJpaRepositories(basePackages = {
        "homeWork6.repo",   // репозитории продуктов
})
@EntityScan(basePackages = {
        "homeWork6.domain", // сущности продуктов/заметок
})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}