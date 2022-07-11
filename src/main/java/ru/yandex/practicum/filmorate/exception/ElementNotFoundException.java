package ru.yandex.practicum.filmorate.exception;

public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(long id, String type) {
        super("Элемент " + type + " не найден: id=" + id);
    }
}
