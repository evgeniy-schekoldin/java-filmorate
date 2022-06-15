package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(long id) {
        super("Пользователь не найден: id=" + id);
    }

}
