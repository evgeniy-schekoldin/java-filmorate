package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film GetFilmById(long id) {
        return filmStorage.getFilm(id);
    }

    public void addRate(long filmId, long userId) {
        filmStorage.getFilm(filmId).addRate(userStorage.getUser(userId).getId());
        log.info("Добавлена оценка фильма id={} пользователем userId={}", filmId, userId);
    }

    public void removeRate(long filmId, long userId) {
        filmStorage.getFilm(filmId).removeRate(userStorage.getUser(userId).getId());
        log.info("Удалена оценка фильма id={} пользователем userId={}", filmId, userId);
    }

    public List<Film> getMostRated(int count) {
        return filmStorage.getFilms()
                .stream()
                .sorted((f0, f1) -> (f0.getLikes().size() - f1.getLikes().size()) * -1)
                .limit(count)
                .collect(Collectors.toList());
    }

}
