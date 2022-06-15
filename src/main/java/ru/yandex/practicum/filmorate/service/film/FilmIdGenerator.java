package ru.yandex.practicum.filmorate.service.film;

public class FilmIdGenerator {

    private static long id;

    public static long generate() {
        return ++id;
    }

}
