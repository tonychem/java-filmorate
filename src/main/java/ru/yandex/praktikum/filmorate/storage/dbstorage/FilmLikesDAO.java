package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO-класс для работы с таблицей FILM_LIKES
 */
@Repository
@RequiredArgsConstructor
public class FilmLikesDAO {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Возвращает список id пользователей, которые лайкнули фильм
     *
     * @param filmId - id фильма
     * @return список id пользователей
     */
    public List<Long> userLikesByFilm(long filmId) {
        return jdbcTemplate.queryForList("SELECT user_id FROM film_likes WHERE film_id = ?", long.class,
                filmId);
    }

    /**
     * Возвращает список из id наиболее популярных фильмов в порядке убывания
     *
     * @param limit - количество фильмов в списке
     * @return cписок id фильмов
     */
    public List<Long> mostLikedFilms(int limit) {
        //Сначала формируется список наиболее популярных фильмов очевидным образом (есть записи в таблице FILM_LIKES)
        String query = "SELECT film_id FROM film_likes GROUP BY film_id ORDER BY COUNT(user_id) DESC LIMIT ?";
        List<Long> naiveFilmList = jdbcTemplate.queryForList(query, long.class, limit);
        //Если размера сформированного списка не хватает, тогда добавляем те, которых нет в таблице (т.е. 0 лайков)
        if (limit > naiveFilmList.size()) {
            //сколько не хватает до формирования списка
            int numberToAdd = limit - naiveFilmList.size();

            if (naiveFilmList.size() == 0) {
                String queryForAnyFilms = "SELECT film_id FROM films LIMIT ?";
                List<Long> additiveList = jdbcTemplate.queryForList(queryForAnyFilms, long.class, numberToAdd);
                return additiveList;
            }

            StringBuilder idListBuilder = new StringBuilder();
            for (Long filmId : naiveFilmList) {
                idListBuilder.append(filmId + ",");
            }
            idListBuilder.deleteCharAt(idListBuilder.length() - 1);
            String idList = idListBuilder.toString();

            String queryForRandomNoLikeFilms = String.format("SELECT film_id FROM films WHERE film_id " +
                    "NOT IN (%s) LIMIT %d", idList, numberToAdd);

            List<Long> additiveFilmList = jdbcTemplate.queryForList(queryForRandomNoLikeFilms, long.class);
            naiveFilmList.addAll(additiveFilmList);
        }

        return naiveFilmList;
    }

    public boolean addLike(long filmId, long userId) {
        return jdbcTemplate.update("INSERT INTO film_likes VALUES (?, ?)", filmId, userId) == 1;
    }

    public boolean removeLike(long filmId, long userId) {
        return jdbcTemplate.update("DELETE FROM film_likes WHERE film_id = ? AND user_id = ?", filmId, userId) == 1;
    }

    public boolean checkKeyExists(long filmId, long userId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT COUNT(*) FROM film_likes WHERE film_id = ? AND user_id = ?",
                filmId, userId);
        if (rowSet.next()) {
            return rowSet.getInt(1) == 1;
        }
        return false;
    }
}
