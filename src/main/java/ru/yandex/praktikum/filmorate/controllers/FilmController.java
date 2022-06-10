package ru.yandex.praktikum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;
import ru.yandex.praktikum.filmorate.validation.ValidationException;
import ru.yandex.praktikum.filmorate.validation.Validator;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {
    private final Validator validator;

    private final FilmStorage filmStorage;

    public FilmController(Validator validator, FilmStorage filmStorage) {
        this.validator = validator;
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.films();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        //Если объект валидный - присвоить id и записать в таблицу, иначе - записать в лог и выбросить исключение
        try {
            validator.validateRequestBody(film);
        } catch (ValidationException e) {
            log.warn(e.getMessage() + "\n" + film);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Film filmIdUpdated = filmStorage.addFilm(film);
        log.info("Фильм с названием {} был добавлен", filmIdUpdated.getName());

        return filmIdUpdated;
    }

    // Посколько id назначается контроллером, считаем, что у старого и нового фильма совпадают хотя бы названия
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        //Если объект валидный - присвоить id и записать в таблицу, иначе - записать в лог и выбросить исключение
        try {
            validator.validateRequestBody(film);
        } catch (ValidationException e) {
            log.warn(e.getMessage() + "\n" + film);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Film filmToBeUpdated = filmStorage.updateFilm(film);

        if (filmToBeUpdated != null) {
            log.info("Фильм с названием {} был обновлен", filmToBeUpdated.getName());
            return filmToBeUpdated;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}

