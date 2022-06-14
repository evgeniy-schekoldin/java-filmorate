package ru.yandex.practicum.filmorate.service;

public class FilmIdGenerator {

    private static long id;

    public static long generate() {
        return ++id;
    }

}
