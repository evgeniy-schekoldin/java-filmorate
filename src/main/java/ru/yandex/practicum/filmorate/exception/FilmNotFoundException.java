package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends Exception {

    public FilmNotFoundException(long id) {
        super("Не найден элемент: id=" + id);
    }

}
