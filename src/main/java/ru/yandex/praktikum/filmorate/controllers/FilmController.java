package ru.yandex.praktikum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.service.FilmService;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;
import ru.yandex.praktikum.filmorate.validation.Validator;

import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/films")
@Slf4j
@Validated
public class FilmController {
    private final Validator validator;
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    public FilmController(Validator validator,
                          @Qualifier(value = "FilmServiceDB") FilmService filmService,
                          @Qualifier(value = "FilmDBStorage") FilmStorage filmStorage) {
        this.validator = validator;
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.films();
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable @Positive long id) {
        return filmStorage.filmById(id);
    }

    @GetMapping(value = "/popular")
    public List<Film> getMostLikedFilms(@RequestParam(required = false) @Positive Integer count) {
        if (count != null) {
            return filmService.getMostLikedFilms(count);
        } else {
            return filmService.getMostLikedFilms(10);
        }
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validator.validateRequestBody(film);
        System.out.println(film);
        Film filmIdUpdated = filmStorage.addFilm(film);
        log.info("Фильм с названием {} был добавлен", filmIdUpdated.getName());
        return filmIdUpdated;

    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void hitLike(@PathVariable @Positive long id,
                        @PathVariable @Positive long userId) {
        filmService.hitLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLike(@PathVariable @Positive long id,
                           @PathVariable @Positive long userId) {
        filmService.removeLike(id, userId);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validator.validateRequestBody(film);
        Film filmToBeUpdated = filmStorage.updateFilm(film);
        log.info("Фильм с названием {} был обновлен", filmToBeUpdated.getName());
        return filmToBeUpdated;
    }
}

