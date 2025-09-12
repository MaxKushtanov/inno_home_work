package homeWork4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void registerUser(String username) {
        if (username.length() < 3
                || username.length() > 30
                || !username.matches("^[a-zA-Z0-9_]+$")) {
            throw new ValidationException("Ошибка валидации userName");
        }
        User user = new User();
        user.setUsername(username);
        userDao.create(user);
        logger.info("зарегистрирован пользователь " + username);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void renameUser(Long id, String newUsername) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(newUsername);
            userDao.update(user);
            logger.info("Обновлено имя пользователя с id =  " + id);
        } else {
            logger.info("Не найден пользователь с id = " + id);
        }
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isPresent()) {
            userDao.delete(id);
            logger.info("Удален пользователь " + optionalUser.get().getUsername());
        } else {
            logger.info("Не найден пользователь с id = " + id);
        }
    }
}