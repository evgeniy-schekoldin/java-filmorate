package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    UserStorage userStorage;
    FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addRate(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        filmStorage.getFilm(filmId).addRate(userStorage.getUser(userId).getId());
        log.info("Добавлена оценка фильма id={} пользователем userId={}", filmId, userId);
    }

    public void removeRate(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        filmStorage.getFilm(filmId).removeRate(userStorage.getUser(userId).getId());
        log.info("Удалена оценка фильма id={} пользователем userId={}", filmId, userId);
    }

    public List<Film> getMostRated(int count) {

        return filmStorage.getFilms()
                .stream()
                .sorted((f0, f1) -> (f0.getRated().size() - f1.getRated().size()) * -1)
                .limit(count)
                .collect(Collectors.toList());
    }

}
