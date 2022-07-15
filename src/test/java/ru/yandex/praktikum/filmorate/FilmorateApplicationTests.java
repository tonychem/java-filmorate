package ru.yandex.praktikum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.model.MPA;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(scripts = {"/schema-test.sql", "/data-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/droptables-test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class FilmoRateApplicationTests {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmoRateApplicationTests(@Qualifier(value = "UserDBStorage") UserStorage userStorage, @Qualifier(value = "FilmDBStorage") FilmStorage filmStorage) {
        this.userStorage = userStorage;
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

    @Test
    public void testFindUserById() {

        userStorage.addUser(user1);

        Optional<User> userOptional = Optional.of(userStorage.userById(1));

        assertThat(userOptional).isPresent().hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 1l));
    }

    @Test
    public void testUpdateUser() {
        userStorage.addUser(user1);
        userStorage.updateUser(user1Update);
        Optional<User> userOptional = Optional.of(userStorage.userById(1));

        assertThat(userOptional).isPresent().hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("email", "mewEmail@ya.ru"));
    }

    @Test
    public void testFindFilmById() {
        filmStorage.addFilm(film1);

        Optional<Film> filmOptional = Optional.of(filmStorage.filmById(1l));

        assertThat(filmOptional).isPresent().hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", 1l));
    }

    @Test
    public void testUpdateFilm() {
        filmStorage.addFilm(film1);
        filmStorage.updateFilm(updateForFilm1);

        Optional<Film> filmOptional = Optional.of(filmStorage.filmById(1));

        assertThat(filmOptional).isPresent().hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", "update"));
    }

}
