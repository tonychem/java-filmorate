package ru.yandex.praktikum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.praktikum.filmorate.exception.NoSuchFilmException;
import ru.yandex.praktikum.filmorate.exception.NoSuchUserException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    //Отображение Id фильма на множество Id пользователей, лайкнувших фильм
    private final Map<Long, Set<Long>> filmLikeMap = new HashMap<>();

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void hitLike(long filmId, long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NoSuchFilmException(String.format("Фильм с id = %d отсутствует", filmId));
        }

        if (userStorage.getUserById(userId) == null) {
            throw new NoSuchUserException(String.format("Пользователя с id = %d не существует", userId));
        }

        if (!filmLikeMap.containsKey(filmId)) {
            filmLikeMap.put(filmId, new HashSet<>());
        }
        filmLikeMap.get(filmId).add(userId);
    }

    public void removeLike(long filmId, long userId) {
        Set<Long> likesSet = filmLikeMap.get(filmId);

        if (likesSet != null) {
            likesSet.remove(userId);
        } else {
            checkFilm(filmId);
        }
    }

    public List<Film> getMostLikedFilms(int limit) {
        // сравниваем пары значений (filmId, {userId}) по размеру множества лайков, сортируем в порядке убывания
        List<Film> listOfMostPopularFilmsCachedInLikeMap = filmLikeMap.entrySet().stream()
                .sorted((entry1, entry2) -> (-1) * Long.compare(entry1.getValue().size(), entry2.getValue().size()))
                .limit(limit)
                .map(x -> filmStorage.getFilmById(x.getKey()))
                .collect(Collectors.toList());

        // Поскольку фильмы не кешируются в filmLikeMap до тех пор, пока не был вызван метод hitLike,
        // необходимо добавить фильмы в конец списка популярных фильмов из числа тех, которые отсутствуют в filmLikeMap
        if (listOfMostPopularFilmsCachedInLikeMap.size() < limit
                && filmLikeMap.size() < filmStorage.films().size()) {
            int remainder = filmStorage.films().size() - filmLikeMap.size();

            List<Film> addUpList = filmStorage.films().stream()
                    .filter(x -> !filmLikeMap.containsKey(x.getId()))
                    .limit(remainder)
                    .collect(Collectors.toList());

            listOfMostPopularFilmsCachedInLikeMap.addAll(addUpList);
        }

        return listOfMostPopularFilmsCachedInLikeMap;
    }

    private void checkFilm(long filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NoSuchFilmException(String.format("Фильм с id = %d отсутствует", filmId));
        }
    }
}
