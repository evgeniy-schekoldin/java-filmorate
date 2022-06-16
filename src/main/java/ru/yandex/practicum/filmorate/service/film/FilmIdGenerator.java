package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Component;

@Component
public class FilmIdGenerator {

    private static long id;

    public long generate() {
        return ++id;
    }

}
