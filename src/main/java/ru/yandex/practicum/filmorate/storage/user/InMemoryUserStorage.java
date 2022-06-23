package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.service.user.UserIdGenerator;
import ru.yandex.practicum.filmorate.service.user.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final UserIdGenerator userIdGenerator;
    private final UserValidator userValidator;

    public InMemoryUserStorage(UserIdGenerator userIdGenerator, UserValidator userValidator) {
        this.userIdGenerator = userIdGenerator;
        this.userValidator = userValidator;
    }

    @Override
    public User addUser(User user) {
        userValidator.validate(user);
        if (users.values().stream().filter(u -> u.getEmail().equals(user.getEmail())).count() > 0) {
            log.error("Пользователь уже существует: email={}", user.getEmail());
            throw new UserAlreadyExistException(user.getEmail());
        }
        user.setId(userIdGenerator.generate());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}, присвоен id={}", user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        userValidator.validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновлен пользователь: {}, id={}", user.getLogin(), user.getId());
            return user;
        }
        log.error("Не найден пользователь: {}, id={}", user.getLogin(), user.getId());
        throw new UserNotFoundException(user.getId());
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    @Override
    public User getUser(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        }
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

}