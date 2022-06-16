package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(long id) {
        super("Фильм не найден: id=" + id);
    }

}
