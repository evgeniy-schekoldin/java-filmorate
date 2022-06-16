package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistException extends RuntimeException  {

    public UserAlreadyExistException(String email) {
        super("Пользователь уже существует: email=" + email);
    }

}
