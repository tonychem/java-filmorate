package ru.yandex.praktikum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    //Отображение Id фильма на множество Id пользователей, лайкнувших фильм
    private final Map<Long, Set<Long>> filmLikeMap = new HashMap<>();

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void hitLike(Film film, User user) {
        long filmId = film.getId();
        long userId = user.getId();

        if (!filmLikeMap.containsKey(filmId)) {
            filmLikeMap.put(filmId, new HashSet<>());
        }

        filmLikeMap.get(filmId).add(userId);
    }

    public void removeLike(Film film, User user) {
        long filmId = film.getId();
        long userId = user.getId();

        Set<Long> likesSet = filmLikeMap.get(filmId);

        if (likesSet != null) {
            filmLikeMap.remove(userId);
        } else {
            throw new NoLikesException(String.format("У фильма %s отсутствуют лайки", film.getName()));
        }
    }

    public List<Film> getTenMostLikedFilms() {
        // сравниваем пары значений (filmId, {userId}) по размеру множества лайков, сортируем в порядке убывания
        return filmLikeMap.entrySet().stream()
                .sorted((entry1, entry2) -> (-1) * Long.compare(entry1.getValue().size(), entry2.getValue().size()))
                .limit(10)
                .map(x -> filmStorage.getFilmById(x.getKey()))
                .collect(Collectors.toList());
    }
}
