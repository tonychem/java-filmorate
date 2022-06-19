package ru.yandex.praktikum.filmorate.storage;

import ru.yandex.praktikum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film filmById(long id);
    Collection<Film> films();
    Film addFilm(Film Film);
    boolean deleteFilm(long id);
    Film updateFilm(Film film);
}
