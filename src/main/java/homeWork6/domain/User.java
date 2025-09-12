package homeWork6.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        indexes = @Index(name = "ux_users_username", columnList = "username", unique = true)
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[A-Za-z0-9_]+$")
    private String username;

    // НОВОЕ: продукты пользователя
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) {
        products.add(p);
        p.setUser(this);
    }

    public void removeProduct(Product p) {
        products.remove(p);
        p.setUser(null);
    }

    // getters/setters/toString/equals/hashCode
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }
}