package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String email) {
        super("Пользователь уже существует: email=" + email);
    }

}
