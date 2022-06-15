package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.user.UserIdGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final static String AT_SYMBOL = "@";
    private final static String SPACE_SYMBOL = " ";

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) throws ValidationException, UserAlreadyExistException {
        validate(user);
        if (users.values().stream().filter(u -> u.getEmail().equals(user.getEmail())).count() > 0) {
            log.error("Пользователь уже существует: email={}", user.getEmail());
            throw new UserAlreadyExistException(user.getEmail());
        }
        user.setId(UserIdGenerator.generate());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}, присвоен id={}", user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException, UserNotFoundException {
        validate(user);
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
    public User getUser(long id) throws UserNotFoundException {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        }
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private void validate(User user) throws ValidationException {
        ValidationException ex;
        if (user.getEmail().isEmpty() || !user.getEmail().contains(AT_SYMBOL)) {
            ex = new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            log.error(ex.getMessage());
            throw ex;
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(SPACE_SYMBOL)) {
            ex = new ValidationException("Логин не может быть пустым и содержать пробелы");
            log.error(ex.getMessage());
            throw ex;
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            ex = new ValidationException("Дата рождения не может быть в будущем");
            log.error(ex.getMessage());
            throw ex;
        }
    }

}
