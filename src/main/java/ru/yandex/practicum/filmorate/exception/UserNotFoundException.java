package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.User;

public class UserNotFoundException extends Throwable {

    public UserNotFoundException(User user) {
        super("Не найден: id=" + user.getId() + ", login=" + user.getLogin());
    }

}
