package ru.yandex.praktikum.filmorate.validation;

import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Validator {
    public boolean validateRequestBody(Film film) {
        LocalDate releaseDateLowerBound = LocalDate.of(1895, 12, 28);
        LocalDate actualReleaseDate = LocalDate.parse(film.getReleaseDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return !(film.getName().isEmpty()) && (film.getDescription().length() <= 200)
                && (actualReleaseDate.isAfter(releaseDateLowerBound)) && (film.getDuration() > 0);
    }
    public boolean validateRequestBody(User user) {
        LocalDate now = LocalDate.now();
        LocalDate actualBirthday = LocalDate.parse(user.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return !(user.getEmail().isEmpty()) && (user.getEmail().contains("@")) && !(user.getLogin().isEmpty())
                && !(user.getLogin().contains(" ")) && (actualBirthday.isBefore(now));
    }
}
