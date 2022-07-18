package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmDao filmDao;
    private final FilmValidator filmValidator;

    public FilmService(FilmDao filmDao, FilmValidator filmValidator) {
        this.filmDao = filmDao;
        this.filmValidator = filmValidator;
    }

    public Film addFilm(Film film) {
        filmValidator.validate(film);
        log.info("Добавление фильма id={}", film.getId());
        return filmDao.addFilm(film);
    }

    public Film updateFilm(Film film) {
        filmValidator.validate(film);
        log.info("Обновление фильма id={}", film.getId());
        return filmDao.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmDao.getFilms();
    }

    public Film GetFilm(long id) {
        return filmDao.getFilm(id);
    }

    public void addLike(long filmId, long userId) {
        filmDao.addLike(filmId, userId);
        log.info("Добавлена оценка фильма id={} пользователем userId={}", filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmDao.removeLike(filmId, userId);
        log.info("Удалена оценка фильма id={} пользователем userId={}", filmId, userId);
    }

    public List<Film> getMostRated(int count) {
        return filmDao.getMostRated(count);
    }

    public List<Mpa> getMpas() {
        return filmDao.getMpas();
    }

    public Mpa getMpa(int id) {
        return filmDao.getMpa(id);
    }

    public List<Genre> getGenres() {
        return filmDao.getGenres();
    }

    public Genre getGenre(int id) {
        return filmDao.getGenre(id);
    }
}
