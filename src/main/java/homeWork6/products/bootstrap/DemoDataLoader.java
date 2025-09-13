package homeWork6.products.bootstrap;

import homeWork6.products.domain.Product;
import homeWork6.products.domain.User;
import homeWork6.products.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final UserRepository users;

    public DemoDataLoader(UserRepository users) {
        this.users = users;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // создаём пользователя с двумя заметками
        User u = new User();
        u.setUsername("alice_01");

        // НОВОЕ: добавим 2 продукта этому пользователю
        Product p1 = new Product();
        p1.setAccountNumber("ACC-001");
        p1.setBalance(new BigDecimal("1234.56"));
        p1.setType("ACCOUNT");
        u.addProduct(p1);

        Product p2 = new Product();
        p2.setAccountNumber("CARD-4111");
        p2.setBalance(new BigDecimal("99.99"));
        p2.setType("CARD");
        u.addProduct(p2);

        users.save(u); // каскадом сохранятся и заметки, и продукты

        // читаем обратно и трогаем LAZY-коллекции
        users.findByUsername("alice_01").ifPresent(loaded -> {
            System.out.println("Loaded user: " + loaded);
            System.out.println("Products count: " + loaded.getProducts().size()); // отдельный SELECT
        });
    }
}