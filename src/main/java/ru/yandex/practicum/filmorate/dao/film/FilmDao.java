package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface FilmDao {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(long id);

    List<Film> getFilms();

    List<Film> getMostRated(int count);

    void addLike(long film_id, long user_id);

    void removeLike(long film_id, long user_id);

    Mpa getMpa(int id);

    List<Mpa> getMpas();

    Genre getGenre(int id);

    List<Genre> getGenres();

}
