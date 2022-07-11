package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserDao;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserDao userDao;
    private final UserValidator userValidator;

    public UserService(UserDao userDao, UserValidator userValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
    }

    public User addUser(User user) {
        userValidator.validate(user);
        log.info("Добавление пользователя id={}", user.getId());
        return userDao.addUser(user);
    }

    public User updateUser(User user) {
        userValidator.validate(user);
        log.info("Обновление пользователя id={}", user.getId());
        return userDao.updateUser(user);
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public User GetUserById(long id) {
        return userDao.getUser(id);
    }

    public void addFriend(long id, long friendId) {
        userDao.addFriend(id, friendId);
        log.info("Добавление в друзья id={}, friendId={}", id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
       userDao.removeFriend(id, friendId);
       log.info("Удаление из друзей id={}, friendId={}", id, friendId);
    }

    public List<User> getFriends(long id) {
        return userDao.getFriends(id);
    }

    public List<User> getCommonFriends(long id, long otherId) {
        return userDao.getCommonFriends(id, otherId);
    }

}
