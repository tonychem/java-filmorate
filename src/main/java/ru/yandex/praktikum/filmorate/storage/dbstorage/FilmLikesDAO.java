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

    public List<Long> mostLikedFilms(int limit) {
        //Сначала формируется список наиболее популярных фильмов очевидным образом (есть записи в таблице FILM_LIKES)
        String query = "SELECT film_id FROM film_likes GROUP BY film_id ORDER BY COUNT(user_id) DESC LIMIT ?";
        List<Long> naiveFilmList = jdbcTemplate.queryForList(query, long.class, limit);
        //Если размера сформированного списка не хватает, тогда добавляем те, которых нет в таблице (т.е. 0 лайков)
        if (limit > naiveFilmList.size()) {
            //сколько не хватает до формирования списка
            int numberToAdd = limit - naiveFilmList.size();

            //если в базе нет фильмов с лайками, то выводим любые
            if (naiveFilmList.size() == 0) {
                String queryForAnyFilms = "SELECT film_id FROM films LIMIT ?";
                List<Long> additiveList = jdbcTemplate.queryForList(queryForAnyFilms, long.class, numberToAdd);
                return additiveList;
            }

            //иначе формируем строковое представление id-шников фильмов вида (id1, id2, id3, ..., idN), которые
            //присутствуют в таблице FILM_LIKES, и делаем запрос на произвольные фильмы, не принадлежащие этому диапазону

            String[] filmIdsAsStringArray = new String[naiveFilmList.size()];
            for (int i = 0; i < naiveFilmList.size(); i++) {
                filmIdsAsStringArray[i] = String.valueOf(naiveFilmList.get(i));
            }
            String idList = String.join(",", filmIdsAsStringArray);

            String queryForRandomNoLikeFilms = String.format("SELECT film_id FROM films WHERE film_id " +
                    "NOT IN (%s) LIMIT %d", idList, numberToAdd);

            List<Long> additiveFilmList = jdbcTemplate.queryForList(queryForRandomNoLikeFilms, long.class);
            naiveFilmList.addAll(additiveFilmList);
        }

        return naiveFilmList;
    }

    public void addLike(long filmId, long userId) {
        jdbcTemplate.update("INSERT INTO film_likes VALUES (?, ?)", filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        jdbcTemplate.update("DELETE FROM film_likes WHERE film_id = ? AND user_id = ?", filmId, userId);
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
