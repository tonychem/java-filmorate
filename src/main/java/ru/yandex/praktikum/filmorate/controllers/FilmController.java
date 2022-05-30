package ru.yandex.praktikum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    private final Validator validator;
    //Контроллер присваивает id приходящим объектам
    private long currentId = 1;

    public FilmController(Validator validator) {
        this.validator = validator;
    }

    @GetMapping
    public Map<Long, Film> getFilms() {
        return films;
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

        film.setId(currentId);
        films.put(currentId, film);
        log.info("Фильм с названием {} был добавлен", film.getName());
        currentId++;

        return film;
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

        Optional<Film> filmToBeUpdated = films.values().stream()
                .filter(x -> x.getName().equals(film.getName()))
                .findAny();
        //Если в таблице находится фильм с таким id, то извлекаем данный id, иначе присваиваем текущий id
        long id = filmToBeUpdated.map(Film::getId).orElseGet(() -> currentId++);
        film.setId(id);
        films.put(id, film);
        log.info("Фильм с названием {} был обновлен", film.getName());

        return film;
    }
}

