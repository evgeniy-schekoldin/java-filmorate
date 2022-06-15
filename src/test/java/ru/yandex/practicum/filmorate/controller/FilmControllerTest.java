package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    @Test
    void filmCreationTestWithCorrectInput() throws ValidationException {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        Film film = new Film();
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.of(2000, 01, 01));
        film.setDuration(100);
        FilmController filmController = new FilmController(filmStorage, filmService);
        filmController.addFilm(film);
        int expectedSize = 1;
        int size = filmController.getFilms().size();
        assertEquals(expectedSize, size);
    }

    @Test
    void filmCreationTestWithIncorrectName() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        Film film = new Film();
        film.setName("");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.of(2000, 01, 01));
        film.setDuration(100);
        FilmController filmController = new FilmController(filmStorage, filmService);
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.addFilm(film);
                });
    }

    @Test
    void filmCreationTestWithIncorrectDescription() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        String invalidDescription = "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят " +
        "разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                "который за время «своего отсутствия», стал кандидатом Коломбани.";
        Film film = new Film();
        film.setName("test");
        film.setDescription(invalidDescription);
        film.setReleaseDate(LocalDate.of(2000,01,01));
        film.setDuration(100);
        FilmController filmController = new FilmController(filmStorage, filmService);
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.addFilm(film);
                });
    }

    @Test
    void filmCreationTestWithIncorrectReleaseDate() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        LocalDate invalidDate = LocalDate.of(1900,1,1);
        Film film = new Film();
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(invalidDate);
        film.setDuration(100);
        FilmController filmController = new FilmController(filmStorage, filmService);
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.addFilm(film);
                });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void filmCreationTestWithIncorrectDuration(int invalidDuration) {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        Film film = new Film();
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.of(2000,01,01));
        film.setDuration(invalidDuration);
        FilmController filmController = new FilmController(filmStorage, filmService);
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.addFilm(film);
                });
    }

}