package ru.yandex.praktikum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.validation.Validator;

public class ValidationTests {

    private static Validator validator;

    private static User normalUser;
    private static User userWithEmptyEmail;
    private static User userWithEmailWithoutAt;
    private static User userWithEmptyLogin;
    private static User userLoginWithSpaces;
    private static User birthdayInFuture;

    private static Film normalFilm;
    private static Film filmWithEmptyName;
    private static Film filmWithDescriptionCharactersExceeding200;
    private static Film filmReleasedEarlierThanFilmographyEra;
    private static Film filmWithDurationZero;

    // Блок инициализации тестовых объектов
    @BeforeAll
    public static void init() {
        validator = new Validator();

        normalUser = new User( 1, "team@yandex.ru", "login", "name", "1995-05-30");
        userWithEmptyEmail = new User( 2, "", "login", "name", "1995-05-30");
        userWithEmailWithoutAt = new User( 3, "team/yandex.ru", "login", "name", "1995-05-30");
        userWithEmptyLogin = new User( 4, "team@yandex.ru", "", "name", "1995-05-30");
        userLoginWithSpaces = new User( 5, "team@yandex.ru", "login  double spaced", "name", "1995-05-30");
        birthdayInFuture = new User( 6, "team@yandex.ru", "login", "name", "2032-05-30");

        normalFilm = new Film(1, "nisi eiusmod", "adipisicing", "1967-03-25", 100);
        filmWithEmptyName = new Film(2, "", "adipisicing", "1967-03-25", 100);
        filmWithDescriptionCharactersExceeding200 =
                new Film(3, "nisi eiusmod", "At an abandoned hotel, a police squad corners Trinity, " +
                        "who overpowers them with superhuman abilities. She flees, pursued by the police and a group of " +
                        "suited Agents capable of similar superhuman feats. " +
                        "She answers a ringing public telephone and vanishes. Computer programmer Thomas Anderson, known " +
                        "by his hacking alias \"Neo\", is puzzled by repeated online encounters with " +
                        "the phrase \"the Matrix\". " +
                        "Trinity contacts him and tells him a man named Morpheus has the answers Neo seeks. " +
                        "A team of Agents and police, led by Agent Smith, arrives at Neo's workplace in search of him. " +
                        "Though Morpheus attempts to guide Neo to safety, Neo surrenders rather than risk a dangerous escape. " +
                        "The Agents attempt to coerce Neo into helping them locate Morpheus, who they claim is a terrorist. " +
                        "When Neo refuses, the Agents fuse his mouth shut and implant a robotic \"bug\" in his stomach. " +
                        "Neo wakes up from what he believes to be a nightmare. " +
                        "Soon after, Neo is taken by Trinity to meet Morpheus, and she removes the bug from Neo, indicating " +
                        "that the \"nightmare\" he experienced was apparently real.", "1967-03-25", 100);
        filmReleasedEarlierThanFilmographyEra = new Film(3, "nisi eiusmod", "adipisicing",
                "1895-12-27", 100);
        filmWithDurationZero = new Film(4, "nisi eiusmod", "adipisicing",
                "1995-12-27", 0);
    }

    @DisplayName("Valid user")
    @Test
    public void checkValidUser() {
        Assertions.assertTrue(validator.validateRequestBody(normalUser));
    }

    @DisplayName("Valid film")
    @Test
    public void checkValidFilm() {
        Assertions.assertTrue(validator.validateRequestBody(normalFilm));
    }

    @DisplayName("User with empty email")
    @Test
    public void checkUserWithEmptyEmail() {
        Assertions.assertFalse(validator.validateRequestBody(userWithEmptyEmail));
    }

    @DisplayName("User with email without @")
    @Test
    public void checkUserWithEmailWithoutAt() {
        Assertions.assertFalse(validator.validateRequestBody(userWithEmailWithoutAt));
    }

    @DisplayName("User with empty login")
    @Test
    public void checkUserWithEmptyLogin() {
        Assertions.assertFalse(validator.validateRequestBody(userWithEmptyLogin));
    }

    @DisplayName("User login with spaces")
    @Test
    public void checkUserLoginWithSpaces() {
        Assertions.assertFalse(validator.validateRequestBody(userLoginWithSpaces));
    }

    @DisplayName("User birthday in future")
    @Test
    public void checkUserBirthdayInFuture() {
        Assertions.assertFalse(validator.validateRequestBody(birthdayInFuture));
    }

    @DisplayName("Film with empty name")
    @Test
    public void checkFilmWithEmptyName() {
        Assertions.assertFalse(validator.validateRequestBody(filmWithEmptyName));
    }

    @DisplayName("Film description exceeding 200 characters")
    @Test
    public void checkFilmWithDescriptionCharactersExceeding200() {
        Assertions.assertFalse(validator.validateRequestBody(filmWithDescriptionCharactersExceeding200));
    }

    @DisplayName("Film released earlier than filmography era")
    @Test
    public void checkFilmReleasedEarlierThanFilmographyEr() {
        Assertions.assertFalse(validator.validateRequestBody(filmReleasedEarlierThanFilmographyEra));
    }

    @DisplayName("Film with 0 duration")
    @Test
    public void checkFilmWithDurationZero() {
        Assertions.assertFalse(validator.validateRequestBody(filmWithDurationZero));
    }
}
