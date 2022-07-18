package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component("FilmDaoImp")
public class FilmDaoImpl implements FilmDao {

    private final static String FILMS_FILM_ID_COLUMN = "film_id";
    private final static String FILMS_FILM_NAME_COLUMN = "film_name";
    private final static String FILMS_FILM_DESCRIPTION_COLUMN = "description";
    private final static String FILMS_FILM_RELEASE_DATE_COLUMN = "release_date";
    private final static String FILMS_FILM_DURATION_COLUMN = "duration";
    private final static String FILMS_FILM_MPA_ID_COLUMN = "mpa_id";
    private final static String MPA_VALUES_MPA_ID_COLUMN = "mpa_id";
    private final static String MPA_VALUES_MPA_NAME_COLUMN = "mpa_name";
    private final static String FILM_GENRES_GENRE_ID = "genre_id";
    private final static String GENRE_VALUES_GENRE_NAME = "genre_name";
    private final static String DATE_FORMATTER = "yyyy-MM-d";

    private final JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT " +
                     "INTO films(film_name, description, release_date, duration, mpa_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        KeyHolder filmId = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, new String[]{FILMS_FILM_ID_COLUMN});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setString(3, film.getReleaseDate().toString());
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, filmId);
        film.setId(filmId.getKey().longValue());
        if (film.getGenres()!=null) {
            String newSql = "INSERT INTO film_genres(film_id, genre_id) " +
                            "VALUES (?,?)";
            film.getGenres().forEach(genre -> jdbcTemplate.update(newSql, film.getId(), genre.getId()));
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films " +
                     "SET film_name=?, description=?, release_date=?, duration=?, mpa_id=? " +
                     "WHERE film_id=?";
        int affectedRows = jdbcTemplate.update(sql,
                           film.getName(),
                           film.getDescription(),
                           film.getReleaseDate(),
                           film.getDuration(),
                           film.getMpa().getId(),
                           film.getId());
        if (affectedRows == 0) throw new FilmNotFoundException(film.getId());
        sql = "DELETE FROM film_genres " +
              "WHERE film_id=?";
        jdbcTemplate.update(sql, film.getId());
        if (film.getGenres() != null) {
            String newSql = "INSERT INTO film_genres(film_id, genre_id) " +
                            "VALUES (?,?)";
            film.getGenres().stream().
                    distinct().
                    forEach(genre -> jdbcTemplate.update(newSql, film.getId(), genre.getId()));
        }
        return film;
    }

    @Override
    public Film getFilm(long id) {
        String sql = "SELECT * FROM films " +
                     "WHERE film_id=?";
        List<Film> result = jdbcTemplate.query(sql, this::makeFilm, id);
        if (result.size() > 0) {
            return result.get(0);
        }
        throw new FilmNotFoundException(id);
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * " +
                     "FROM films";
        return jdbcTemplate.query(sql, this::makeFilm);
    }

    @Override
    public List<Film> getMostRated(int count) {
        String sql = "SELECT f.* " +
                     "FROM films AS f " +
                     "LEFT JOIN film_likes AS fl ON fl.film_id = f.film_id " +
                     "GROUP BY f.film_id, f.film_name, f.description, f.release_date, f.duration, f.mpa_id " +
                     "ORDER BY COUNT(fl.film_id) DESC " +
                     "LIMIT ?";
        return jdbcTemplate.query(sql, this::makeFilm, count);
    }

    @Override
    public void addLike(long film_id, long user_id) {
        String sql = "INSERT INTO film_likes (film_id, user_id) " +
                     "VALUES (?,?)";
        jdbcTemplate.update(sql, film_id, user_id);
    }

    @Override
    public void removeLike(long film_id, long user_id) {
        String sql = "DELETE FROM film_likes " +
                     "WHERE film_id=? AND user_id=?";
        if (jdbcTemplate.update(sql, film_id, user_id) == 0) throw new UserNotFoundException(0);
    }

    @Override
    public Mpa getMpa(int id) {
        String sql = "SELECT * FROM mpa_values " +
                     "WHERE mpa_id=?";
        List<Mpa> result = jdbcTemplate.query(sql, this::makeMpa, id);
        if (result.size() > 0) {
            return result.get(0);
        }
        throw new ElementNotFoundException(id, "MPA");
    }

    @Override
    public List<Mpa> getMpas() {
        String sql = "SELECT * FROM mpa_values " +
                     "ORDER BY mpa_id";
        return jdbcTemplate.query(sql, this::makeMpa);
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "SELECT * FROM genre_values " +
                     "WHERE genre_id=?";
        List<Genre> result = jdbcTemplate.query(sql, this::makeGenre, id);
        if (result.size() > 0) {
            return result.get(0);
        }
        throw new ElementNotFoundException(id, "GENRE");
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genre_values " +
                     "ORDER BY genre_id";
        return jdbcTemplate.query(sql, this::makeGenre);
    }

    private Film makeFilm(ResultSet result, int row) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        Film film = new Film();
        film.setId(result.getLong(FILMS_FILM_ID_COLUMN));
        film.setName(result.getString(FILMS_FILM_NAME_COLUMN));
        film.setDescription(result.getString(FILMS_FILM_DESCRIPTION_COLUMN));
        film.setReleaseDate(LocalDate.parse(result.getString(FILMS_FILM_RELEASE_DATE_COLUMN), formatter));
        film.setDuration(result.getInt(FILMS_FILM_DURATION_COLUMN));
        String sql = "SELECT * FROM mpa_values " +
                     "WHERE mpa_id=?";
        film.setMpa(jdbcTemplate.query(sql, this::makeMpa, result.getInt(FILMS_FILM_MPA_ID_COLUMN)).get(0));
        sql = "SELECT fg.genre_id, gv.genre_name " +
              "FROM film_genres AS fg " +
              "JOIN genre_values AS gv ON gv.genre_id = fg.genre_id " +
              "WHERE fg.film_id = ?";
        film.setGenres(jdbcTemplate.query(sql, this::makeGenre, result.getLong(FILMS_FILM_ID_COLUMN)));
        return film;
    }

    private Mpa makeMpa(ResultSet result, int row) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(result.getInt(MPA_VALUES_MPA_ID_COLUMN));
        mpa.setName(result.getString(MPA_VALUES_MPA_NAME_COLUMN));
        return mpa;
    }

    private Genre makeGenre(ResultSet result, int row) throws SQLException {
        Genre genre = new Genre();
        genre.setId(result.getInt(FILM_GENRES_GENRE_ID));
        genre.setName(result.getString(GENRE_VALUES_GENRE_NAME));
        return genre;
    }

}