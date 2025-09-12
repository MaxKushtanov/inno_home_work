package homeWork5;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "ux_users_username", columnList = "username", unique = true)
        }
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

    // Владелец связи — Note (поле author). Здесь обратная сторона.
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,       // при создании/удалении пользователя каскадно трогаем его заметки
            orphanRemoval = true,            // удаление из коллекции => DELETE у «сироты»
            fetch = FetchType.LAZY
    )
    private List<Note> notes = new ArrayList<>();

    // Утилитарные методы поддерживают «двустороннюю» связь в памяти.
    public void addNote(Note note) {
        notes.add(note);
        note.setAuthor(this);
    }

    public void removeNote(Note note) {
        notes.remove(note);
        note.setAuthor(null);
    }

    // --- getters/setters ---

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Note> getNotes() {
        return notes;
    }

    // equals/hashCode только по id (когда он уже присвоен)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User other = (User) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        // Не логируем коллекции/LAZY-поля — чтобы не провоцировать LazyInitializationException
        return "User{id=" + id + ", username='" + username + "'}";
    }
}