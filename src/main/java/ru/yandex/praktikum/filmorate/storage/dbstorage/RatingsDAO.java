package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

/**
 * DAO-класс для работы с таблицей RATINGS
 */
@Repository
@RequiredArgsConstructor
public class RatingsDAO {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Получить имя рейтинга
     * @param ratingId - id рейтинга
     * @return Название рейтинга
     */
    public String name(int ratingId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT name WHERE rating_id = ?", ratingId);
        return rs.getString(1);
    }

    /**
     * Добавить рейтинг с названием ratingName
     * @param ratingName - название рейтинга
     * @return true - если рейтинг был успешно добавлен
     */
    public boolean addRating(String ratingName) {
        return jdbcTemplate.update("INSERT INTO RATINGS(name) VALUES (?)", ratingName) == 1;
    }

    /**
     * Удалить рейтинг с id = ratingId
     * @param ratingId - id удаляемого рейтинга
     * @return true - если рейтинг был успешно удален
     */
    public boolean deleteRating(int ratingId) {
        return jdbcTemplate.update("DELETE FROM RATINGS WHERE rating_id = ?", ratingId) == 1;
    }
}
