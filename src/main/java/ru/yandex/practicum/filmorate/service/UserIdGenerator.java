package ru.yandex.practicum.filmorate.service;

public class UserIdGenerator {

    private static long id;

    public static long generate() {
        return ++id;
    }

}
