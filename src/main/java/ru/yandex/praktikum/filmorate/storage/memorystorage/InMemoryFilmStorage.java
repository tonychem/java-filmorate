package ru.yandex.praktikum.filmorate.storage.memorystorage;

import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.praktikum.filmorate.exception.NoSuchFilmException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();
    private long currentId = 1;

    @Override
    public Film filmById(long id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> films() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        if (films.values().contains(film)) {
            throw new FilmAlreadyExistsException(String.format("Фильм %s уже существует", film.getName()));
        } else {
            film.setId(currentId);
            films.put(currentId, film);
            currentId++;
            return film;
        }
    }

    @Override
    public boolean deleteFilm(long id) {
        return films.remove(id) != null;
    }

    @Override
    public Film updateFilm(Film film) {
        long id = film.getId();
        if (films.containsKey(id)) {
            films.put(id, film);
            return film;
        } else {
            throw new NoSuchFilmException(String.format("Фильм с id = %d отсутствует", id));
        }
    }
}
