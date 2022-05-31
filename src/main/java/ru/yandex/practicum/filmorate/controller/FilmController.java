package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        validate(film);
        film.setId(1);
        films.put(1, film);
        log.info("Добавлен элемент: {}", film.getName());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws FilmNotFoundException, ValidationException {
        validate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен элемент: {}", film.getName());
            return film;
        }
        log.error("Не найден элемент id={}, name={}", film.getId(), film.getName());
        throw new FilmNotFoundException(film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film film) throws ValidationException {
        ValidationException ex;
        if (film.getName() == null) {
            ex = new ValidationException("Название не может быть пустым");
            log.error(ex.getMessage());
            throw ex;
        }
        if (film.getDescription().length() > 200) {
            ex = new ValidationException("Максимальная длина описания — 200 символов");
            log.error(ex.getMessage());
            throw ex;
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1966, 12, 28))) {
            ex = new ValidationException("Дата релиза — не раньше 28 декабря 1985 года");
            log.error(ex.getMessage());
            throw ex;
        }
        if (film.getDuration() < 0)  {
            ex = new ValidationException("Продолжительность фильма должна быть положительной.");
            log.error(ex.getMessage());
            throw ex;
        }
    }

}