package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmIdGenerator;
import ru.yandex.practicum.filmorate.service.film.FilmValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final FilmIdGenerator filmIdGenerator;
    private final FilmValidator filmValidator;
    private final Map<Long, Film> films = new HashMap<>();

    public InMemoryFilmStorage(FilmIdGenerator filmIdGenerator, FilmValidator filmValidator) {
        this.filmIdGenerator = filmIdGenerator;
        this.filmValidator = filmValidator;
    }

    @Override
    public Film addFilm(Film film) {
        filmValidator.validate(film);
        film.setId(filmIdGenerator.generate());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}, присвоен id={}", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidator.validate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм: {}, id={}", film.getName(), film.getId());
            return film;
        }
        log.error("Не найден фильм: {}, id={}", film.getName(), film.getId());
        throw new FilmNotFoundException(film.getId());
    }

    @Override
    public Film deleteFilm(Film film) {
        return null;
    }

    @Override
    public Film getFilm(long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(id);
        }
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

}
