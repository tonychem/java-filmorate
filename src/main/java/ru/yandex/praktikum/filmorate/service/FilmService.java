package ru.yandex.praktikum.filmorate.service;

import ru.yandex.praktikum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void hitLike(long filmId, long userId);
    void removeLike(long filmId, long userId);
    List<Film> getMostLikedFilms(int limit);

}
