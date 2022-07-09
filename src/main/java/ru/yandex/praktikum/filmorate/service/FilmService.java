package ru.yandex.praktikum.filmorate.service;

import ru.yandex.praktikum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    public void hitLike(long filmId, long userId);
    public void removeLike(long filmId, long userId);
    List<Film> getMostLikedFilms(int limit);

}
