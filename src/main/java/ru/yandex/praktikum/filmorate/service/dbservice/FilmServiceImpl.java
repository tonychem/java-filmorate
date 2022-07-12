package ru.yandex.praktikum.filmorate.service.dbservice;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.service.FilmService;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;
import ru.yandex.praktikum.filmorate.storage.UserStorage;
import ru.yandex.praktikum.filmorate.storage.dbstorage.FilmLikesDAO;

import java.util.List;
import java.util.stream.Collectors;

@Repository(value = "FilmServiceDB")
public class FilmServiceImpl implements FilmService {

    private final FilmLikesDAO filmLikesDAO;
    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public FilmServiceImpl(FilmLikesDAO filmLikesDAO,
                           @Qualifier(value = "FilmDBStorage") FilmStorage filmStorage,
                           @Qualifier(value = "UserDBStorage") UserStorage userStorage) {
        this.filmLikesDAO = filmLikesDAO;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void hitLike(long filmId, long userId) {
        filmLikesDAO.addLike(filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        filmLikesDAO.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getMostLikedFilms(int limit) {
        List<Long> filmIds = filmLikesDAO.mostLikedFilms(limit);
        return filmIds.stream().map(filmStorage::filmById).collect(Collectors.toList());
    }
}
