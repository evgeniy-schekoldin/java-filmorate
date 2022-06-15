package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user) throws ValidationException, UserAlreadyExistException;

    User updateUser(User user) throws ValidationException, UserNotFoundException;

    User deleteUser(User user);

    User getUser(long id) throws UserNotFoundException;

    List<User> getUsers();

}
