package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDao userDao;
    private final FilmDao filmDao;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setName("add_user");
        user.setBirthday(LocalDate.of(1990,1,1));
        user.setEmail("add@user.ru");
        user.setLogin("add@user.ru");
        user = userDao.addUser(user);
        assertThat(userDao.getUser(user.getId())).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setName("update_user");
        user.setBirthday(LocalDate.of(1990,1,1));
        user.setEmail("update@user.ru");
        user.setLogin("update@user.ru");
        user = userDao.addUser(user);
        user.setName("updated");
        assertThat(userDao.updateUser(user)).hasFieldOrPropertyWithValue("name", "updated");
    }

    @Test
    public void testAddFilm() {
        Film fIlm = new Film();
        fIlm.setName("add_film");
        fIlm.setDescription("Added new film");
        fIlm.setDuration(100);
        fIlm.setReleaseDate(LocalDate.of(1999,1,1));
        Mpa mpa = new Mpa();
        mpa.setId(1);
        fIlm.setMpa(mpa);
        filmDao.addFilm(fIlm);
        assertThat(filmDao.getFilm(fIlm.getId())).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testUpdateFilm() {
        Film fIlm = new Film();
        fIlm.setName("update_film");
        fIlm.setDescription("Updated film");
        fIlm.setDuration(100);
        fIlm.setReleaseDate(LocalDate.of(1999,1,1));
        Mpa mpa = new Mpa();
        mpa.setId(1);
        fIlm.setMpa(mpa);
        filmDao.addFilm(fIlm);
        fIlm.setName("updated");
        assertThat(filmDao.updateFilm(fIlm)).hasFieldOrPropertyWithValue("name", "updated");
    }

    @Test
    public void testFindGenreById() {
        Genre genre = filmDao.getGenre(1);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void TestFindAllGenres() {
        assertThat(filmDao.getGenres()).hasSize(6)
                .extracting(Genre::getName).containsExactlyInAnyOrder(
                        "Комедия", "Драма", "Мультфильм", "Ужасы", "Боевик", "Триллер"
                );
    }

    @Test
    public void testFindMpaById() {
        Mpa mpa = filmDao.getMpa(1);
        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void TestFindAllMpa() {
        assertThat(filmDao.getMpas()).hasSize(5)
                .extracting(Mpa::getName).containsExactlyInAnyOrder(
                        "G", "PG-15", "PG-13", "PG-1", "NC-17"
                );
    }

}
