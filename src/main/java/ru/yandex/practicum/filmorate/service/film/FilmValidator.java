package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Component
public class FilmValidator {

    private final static int MAX_FILM_DESCRIPTION_LENGTH = 200;
    private final static LocalDate VALID_FILM_RELEASE_DATE_FROM = LocalDate.of(1966, 12, 28);
    private final static int MIN_FILM_DURATION = 1;

    public void validate(Film film) {
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
