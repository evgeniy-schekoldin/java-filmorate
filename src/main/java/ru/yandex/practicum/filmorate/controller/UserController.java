package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private int id;

    @PostMapping("/users")
    public User addUser(@RequestBody User user) throws ValidationException, UserAlreadyExistException {
        validate(user);
        if (users.values().stream().filter(u -> u.getEmail().equals(user.getEmail())).count() > 0) {
            log.error("Пользователь уже существует: email={}", user.getEmail());
            throw new UserAlreadyExistException(user.getEmail());
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Добавлен пользотваель c id={}, login={}", user.getId(), user.getLogin());
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws ValidationException, UserNotFoundException {
        validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновлен пользотваель id={}, login={}", user.getId(), user.getLogin());
            return user;
        }
        log.error("Не найден пользотваель id={}, login={}", user.getId(), user.getLogin());
        throw new UserNotFoundException(user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private void validate(User user) throws ValidationException {
        ValidationException ex;
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            ex = new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            log.error(ex.getMessage());
            throw ex;
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
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
