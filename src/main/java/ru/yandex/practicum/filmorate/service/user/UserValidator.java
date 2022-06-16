package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@Component
public class UserValidator {

    private final static String AT_SYMBOL = "@";
    private final static String SPACE_SYMBOL = " ";

    public void validate(User user) {
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
