package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void userCreationTestWithCorrectInput() throws ValidationException {
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

    @Test
    void userCreationTestWithIncorrectEmail() throws ValidationException {
        User user = new User();
        user.setEmail("testtest.ru");
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

    @Test
    void userCreationTestWithIncorrectLogin() throws ValidationException {
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin(" ");
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
    void userCreationTestWithIncorrectName() throws ValidationException {
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("test");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserController userController = new UserController();
        userController.addUser(user);
        String expectedMessage = user.getLogin();
        String message = user.getName();
        assertEquals(expectedMessage, message);
    }

    @Test
    void userCreationTestWithIncorrectBirthday() throws ValidationException {
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

}