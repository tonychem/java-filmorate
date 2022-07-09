package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * DAO-класс для работы с таблицей FILM_LIKES
 */
@Repository
@RequiredArgsConstructor
public class FilmLikesDAO {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Возвращает список id пользователей, которые лайкнули фильм
     * @param filmId - id фильма
     * @return список id пользователей
     */
    public List<Long> userLikesByFilm(long filmId) {
        return jdbcTemplate.queryForList("SELECT user_id FROM film_likes WHERE film_id = ?", long.class,
                filmId);
    }

    /**
     * Возвращает список из id наиболее популярных фильмов в порядке убывания
     * @param limit - количество фильмов в списке
     * @return cписок id фильмов
     */
    public List<Long> mostLikedFilms(int limit) {
        String query = "SELECT film_id, COUNT(user_id) likes FROM film_likes GROUP BY film_id ORDER BY likes DESC LIMIT ?";
        return jdbcTemplate.queryForList(query, long.class, limit);
    }
}
