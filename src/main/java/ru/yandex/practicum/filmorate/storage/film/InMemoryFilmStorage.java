package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmIdGenerator;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final static int MAX_FILM_DESCRIPTION_LENGTH = 200;
    private final static LocalDate VALID_FILM_RELEASE_DATE_FROM = LocalDate.of(1966, 12, 28);
    private final static int MIN_FILM_DURATION = 1;

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) throws ValidationException {
        validate(film);
        film.setId(FilmIdGenerator.generate());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}, присвоен id={}", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException, FilmNotFoundException {
        validate(film);
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
    public Film getFilm(long id) throws FilmNotFoundException {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(id);
        }
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film film) throws ValidationException {
        ValidationException ex;
        if (film.getName().isEmpty()) {
            ex = new ValidationException("Название фильма не может быть пустым");
            log.error(ex.getMessage());
            throw ex;
        }
        if (film.getDescription().length() > MAX_FILM_DESCRIPTION_LENGTH) {
            ex = new ValidationException("Максимальная длина описания — " + MAX_FILM_DESCRIPTION_LENGTH + " символов");
            log.error(ex.getMessage());
            throw ex;
        }
        if (film.getReleaseDate().isBefore(VALID_FILM_RELEASE_DATE_FROM)) {
            ex = new ValidationException("Дата релиза — не раньше " + VALID_FILM_RELEASE_DATE_FROM);
            log.error(ex.getMessage());
            throw ex;
        }
        if (film.getDuration() < MIN_FILM_DURATION) {
            ex = new ValidationException("Продолжительность фильма не может быть отрицательной");
            log.error(ex.getMessage());
            throw ex;
        }
    }

}
