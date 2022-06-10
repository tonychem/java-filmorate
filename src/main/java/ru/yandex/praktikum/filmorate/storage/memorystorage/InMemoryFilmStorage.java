package ru.yandex.praktikum.filmorate.storage.memorystorage;

import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();
    private long currentId = 1;

    @Override
    public Film getFilmById(long id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> films() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(currentId);
        films.put(currentId, film);
        currentId++;
        return film;
    }

    @Override
    public boolean deleteFilm(long id) {
        Film filmToBeDeleted = films.remove(id);
        return filmToBeDeleted != null;
    }

    @Override
    public Film updateFilm(Film film) {
        Optional<Film> filmToBeUpdated = films.values().stream()
                .filter(x -> x.getId() == film.getId())
                .findAny();

        Optional<Long> id = filmToBeUpdated.map(Film::getId).or(() -> Optional.empty());

        if (id.isPresent()) {
            film.setId(id.get());
            films.put(id.get(), film);
        } else {
            return null;
        }

        return film;
    }
}
