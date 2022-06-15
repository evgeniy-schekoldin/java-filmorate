package ru.yandex.practicum.filmorate.service.user;

public class UserIdGenerator {

    private static long id;

    public static long generate() {
        return ++id;
    }

}
