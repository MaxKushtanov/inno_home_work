package homeWork7.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "homeWork6.products",
                "homeWork7.payments",
        }
)
@EnableJpaRepositories(basePackages = {
        "homeWork6.products.repo",
        "homeWork7.payments",
})
@EntityScan(basePackages = {
        "homeWork6.products.domain",
        "homeWork7.payments.domain",
})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}