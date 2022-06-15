package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@RestController
public class FilmController {

    FilmStorage filmStorage;
    FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws FilmNotFoundException, ValidationException {
        return filmStorage.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film GetFilmById(@PathVariable long id) throws FilmNotFoundException {
        return filmStorage.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addRate(@PathVariable long id,
                        @PathVariable long userId) throws FilmNotFoundException, UserNotFoundException {
        filmService.addRate(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeRate(@PathVariable long id,
                           @PathVariable long userId) throws FilmNotFoundException, UserNotFoundException {
        filmService.removeRate(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostRated(@RequestParam(defaultValue = "10") int count) {
        return filmService.getMostRated(count);
    }

}