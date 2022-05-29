package ru.yandex.praktikum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.validation.ValidationException;
import ru.yandex.praktikum.filmorate.validation.Validator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();
    private Validator validator = new Validator();
    //Контроллер присваивает id приходящим объектам
    private static long currentId = 1;

    @GetMapping
    public Map<Long, Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (validator.validateRequestBody(film)) {
            film.setId(currentId);
            films.put(currentId, film);
            log.info("Фильм с названием {} был добавлен", film.getName());
            currentId++;
            return film;
        } else {
            String exceptionMessage = "Данные объекта Film неверны. Ошибка при добавлении фильма";
            log.warn(exceptionMessage + "\n" + film);
            throw new ValidationException(exceptionMessage);
        }
    }

    // Посколько id назначается контроллером, считаем, что у старого и нового фильма совпадают хотя бы названия
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (validator.validateRequestBody(film)) {
            Optional<Film> filmToBeUpdated = films.values().stream()
                    .filter(x -> x.getName().equals(film.getName()))
                    .findAny();
            //Если в таблице находится фильм с таким id, то извлекаем данный id, иначе присваиваем текущий id
            long id = filmToBeUpdated.map(Film::getId).orElseGet(() -> currentId++);
            film.setId(id);
            films.put(id, film);
            log.info("Фильм с названием {} был обновлен", film.getName());
            return film;
        } else {
            String exceptionMessage = "Данные объекта Film неверны. Ошибка при обновлении фильма";
            log.warn(exceptionMessage + "\n" + film);
            throw new ValidationException(exceptionMessage);
        }
    }
}

