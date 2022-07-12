package ru.yandex.praktikum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.model.MPA;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmoRateApplicationTests {

    private final UserStorage userStorage;

    private final JdbcTemplate jdbcTemplate;

    private final FilmStorage filmStorage;

    @Autowired
    public FilmoRateApplicationTests(@Qualifier(value = "UserDBStorage") UserStorage userStorage,
                                     JdbcTemplate jdbcTemplate,
                                     @Qualifier(value = "FilmDBStorage") FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
    }

    private static User user1;
    private static User user2;
    private static User user1Update;

    private static Film film1;
    private static Film updateForFilm1;

    @BeforeAll
    public static void init() {
        user1 = new User(1, "next_email@ya.ru", "login2", "user2", "1951-03-03");
        user1Update = new User(1, "mewEmail@ya.ru", "login_login", "user2", "1951-03-03");
        user2 = new User(2, "some_email@ya.ru", "login1", "user1", "1950-01-01");
        film1 = new Film(1, "testFilm", "<>", "1995-06-30", 50, new HashSet<>(), new MPA(1, "someMpa"));
        updateForFilm1 = new Film(1, "update", "<>", "1995-06-30", 50, new HashSet<>(), new MPA(1, "someMpa"));
    }

    @BeforeEach
    public void cleanUp() {
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM films");
    }

    @Test
    public void testFindUserById() {

        long id = userStorage.addUser(user1).getId();

        Optional<User> userOptional = Optional.of(userStorage.userById(id));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", id)
                );
    }

    @Test
    public void testUpdateUser() {
        long idInitial = userStorage.addUser(user1).getId();
        long idAfterUpdate = userStorage.updateUser(user1Update).getId();

        Optional<User> userOptional = Optional.of(userStorage.userById(idAfterUpdate));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "mewEmail@ya.ru")
                );

        assertTrue(idInitial == idAfterUpdate);
    }

    @Test
    public void testFindFilmById() {
        long id = filmStorage.addFilm(film1).getId();

        Optional<Film> filmOptional = Optional.of(filmStorage.filmById(id));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", id)
                );
    }

    @Test
    public void testUpdateFilm() {
        long idInitial = filmStorage.addFilm(film1).getId();
        long idAfterUpdate = filmStorage.updateFilm(updateForFilm1).getId();

        Optional<Film> filmOptional = Optional.of(filmStorage.filmById(idAfterUpdate));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "update")
                );

        assertEquals(idAfterUpdate, idInitial);
    }

}
