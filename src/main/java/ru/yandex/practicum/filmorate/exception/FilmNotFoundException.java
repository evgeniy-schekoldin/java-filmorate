package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.Film;

public class FilmNotFoundException extends Throwable {

    public FilmNotFoundException(Film film) {
        super("Не найден: id=" + film.getId() + " название=" + film.getName());
    }

}
