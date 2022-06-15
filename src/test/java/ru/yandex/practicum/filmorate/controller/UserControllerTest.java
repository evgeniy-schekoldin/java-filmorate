package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void userCreationTestWithCorrectInput() throws ValidationException, UserAlreadyExistException {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController(userStorage, userService);
        userController.addUser(user);
        int expectedSize = 1;
        int size = userController.getUsers().size();
        assertEquals(expectedSize, size);
    }

    @ParameterizedTest
    @ValueSource(strings={"test.ru", ""})
    void userCreationTestWithIncorrectEmail(String invalidEmail) {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        User user = new User();
        user.setEmail(invalidEmail);
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController(userStorage, userService);
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
    }

    @ParameterizedTest
    @ValueSource(strings={"", "lo gin"})
    void userCreationTestWithIncorrectLogin(String invalidLogin) {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin(invalidLogin);
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController(userStorage, userService);
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
    }

    @Test
    void userCreationTestWithIncorrectName() throws ValidationException, UserAlreadyExistException {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController(userStorage, userService);
        userController.addUser(user);
        String expectedMessage = user.getLogin();
        String message = user.getName();
        assertEquals(expectedMessage, message);
    }

    @Test
    void userCreationTestWithIncorrectBirthday() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        LocalDate invalidBirthday = LocalDate.of(2050,1,1);
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(invalidBirthday);
        UserController userController = new UserController(userStorage, userService);
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
    }

    @Test
    void userCreationTestWithAlreadyExistEmail() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        User user1 = new User();
        user1.setEmail("test@test.ru");
        user1.setLogin("user1");
        user1.setName("test");
        user1.setBirthday(LocalDate.of(2000, 01, 01));
        User user2 = new User();
        user2.setEmail("test@test.ru");
        user2.setLogin("user2");
        user2.setName("test");
        user2.setBirthday(LocalDate.of(2000, 01, 01));
        UserController userController = new UserController(userStorage, userService);
        UserAlreadyExistException ex = assertThrows(
                UserAlreadyExistException.class,
                () -> {
                    userController.addUser(user1);
                    userController.addUser(user2);
                });
    }

    @Test
    void userUpdateTestWithCorrectInput() throws UserAlreadyExistException, ValidationException, UserNotFoundException {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController(userStorage, userService);
        userController.addUser(user);
        user.setName("updated");
        userController.updateUser(user);
        User updated = userController.getUsers().get(0);
        String expectedName = user.getName();
        String name = updated.getName();
        assertEquals(expectedName, name);
    }

    @Test
    void userUpdateTestWithIncorrectId() throws UserAlreadyExistException, ValidationException {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController(userStorage, userService);
        userController.addUser(user);
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> {
                    user.setId(2);
                    userController.updateUser(user);
                });
    }

}