package ru.yandex.praktikum.filmorate.validation;

import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Component
public class Validator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate FILM_RELEASE_DATE_LOWER_BOUND = LocalDate.of(1895, 12, 28);

    private static final int MAX_DESCRIPTION_LENGTH = 200;
    public boolean validateRequestBody(Film film) {
        LocalDate actualReleaseDate = LocalDate.parse(film.getReleaseDate(), FORMATTER);
        boolean isValid = !(film.getName().isEmpty()) && (film.getDescription().length() <= MAX_DESCRIPTION_LENGTH)
                && (actualReleaseDate.isAfter(FILM_RELEASE_DATE_LOWER_BOUND)) && (film.getDuration() > 0);

        if (isValid) {
            return true;
        } else {
            throw new ValidationException("Данные объекта Film неверны.");
        }

    }
    public boolean validateRequestBody(User user) {
        LocalDate now = LocalDate.now();
        LocalDate actualBirthday = LocalDate.parse(user.getBirthday(), FORMATTER);

        boolean isValid = !(user.getEmail().isEmpty()) && (user.getEmail().contains("@")) && !(user.getLogin().isEmpty())
                && !(user.getLogin().contains(" ")) && (actualBirthday.isBefore(now));

        // Если объект пользователя валидный - проверить, пустое ли поле name; если да - то установить логин;
        if (isValid) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            return true;
        } else {
            throw new ValidationException("Данные объекта User неверны.");
        }
    }
}
