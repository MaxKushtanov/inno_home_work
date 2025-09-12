package homeWork4;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5433/usersdb");
        config.setUsername("myuser");
        config.setPassword("mypassword");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("MyHikariCP");

        return new HikariDataSource(config);
    }

    @Bean
    public UserDao userDao(DataSource dataSource) {
        return new UserDaoImpl(dataSource);
    }

    @Bean
    public UserService userService(UserDao userDao) {
        return new UserServiceImpl(userDao);
    }
}