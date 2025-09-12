package homeWork4;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(String username);

    Optional<User> getUser(Long id);

    List<User> getAllUsers();

    void renameUser(Long id, String newUsername);

    void deleteUser(Long id);
}