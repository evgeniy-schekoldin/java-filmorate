package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void userCreationTestWithCorrectInput() throws ValidationException, UserAlreadyExistException {
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController();
        userController.addUser(user);
        int expectedSize = 1;
        int size = userController.getUsers().size();
        assertEquals(expectedSize, size);
    }

    @ParameterizedTest
    @ValueSource(strings={"test.ru", ""})
    void userCreationTestWithIncorrectEmail(String email) {
        User user = new User();
        user.setEmail(email);
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController();
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
        String expectedMessage = "Электронная почта не может быть пустой и должна содержать символ @";
        String message = ex.getMessage();
        assertEquals(expectedMessage, message);
    }

    @ParameterizedTest
    @ValueSource(strings={"", "lo gin"})
    void userCreationTestWithIncorrectLogin(String login) {
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin(login);
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController();
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
        String expectedMessage = "Логин не может быть пустым и содержать пробелы";
        String message = ex.getMessage();
        assertEquals(expectedMessage, message);
    }

    @Test
    void userCreationTestWithIncorrectName() throws ValidationException, UserAlreadyExistException {
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController();
        userController.addUser(user);
        String expectedMessage = user.getLogin();
        String message = user.getName();
        assertEquals(expectedMessage, message);
    }

    @Test
    void userCreationTestWithIncorrectBirthday() {
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2050, 01, 01));
        UserController userController = new UserController();
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    userController.addUser(user);
                });
        String expectedMessage = "Дата рождения не может быть в будущем";
        String message = ex.getMessage();
        assertEquals(expectedMessage, message);
    }

    @Test
    void userCreationTestWithAlreadyExistEmail() {
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
        UserController userController = new UserController();
        UserAlreadyExistException ex = assertThrows(
                UserAlreadyExistException.class,
                () -> {
                    userController.addUser(user1);
                    userController.addUser(user2);
                });
        String expectedMessage = "Пользователь уже существует: email=" + user1.getEmail();
        String message = ex.getMessage();
        assertEquals(expectedMessage, message);
    }

    @Test
    void userUpdateTestWithCorrectInput() throws UserAlreadyExistException, ValidationException, UserNotFoundException {
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController();
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
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController();
        userController.addUser(user);
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> {
                    user.setId(2);
                    userController.updateUser(user);
                });
        String expectedMessage = "Не найден: id=" + user.getId() + ", login=" + user.getLogin();
        String message = ex.getMessage();
        assertEquals(expectedMessage, message);
    }

}