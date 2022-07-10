package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO-класс для работы с таблицей RATINGS
 */
@Repository
@RequiredArgsConstructor
public class RatingsDAO {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Получить имя рейтинга
     *
     * @param ratingId - id рейтинга
     * @return Название рейтинга
     */
    public String name(int ratingId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT name FROM ratings WHERE rating_id = ?", ratingId);
        return rs.getString(1);
    }

    public String getRatingName(int ratingId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT name FROM ratings WHERE rating_id = ?", ratingId);
        if (rowSet.next()) {
            return rowSet.getString(1);
        } else {
            throw new RuntimeException(); //TODO: создать кастомную!
        }
    }

    public Map<Integer, String> listOfRatingsInTable() {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM ratings ORDER BY rating_id");
        Map<Integer, String> ratingsMap = new LinkedHashMap<>();

        while (rowSet.next()) {
            Integer ratingId = rowSet.getInt(1);
            String ratingName = rowSet.getString(2);
            ratingsMap.put(ratingId, ratingName);
        }

        return ratingsMap;
    }

    /**
     * Добавить рейтинг с названием ratingName
     *
     * @param ratingName - название рейтинга
     * @return true - если рейтинг был успешно добавлен
     */
    public boolean addRating(String ratingName) {
        return jdbcTemplate.update("INSERT INTO RATINGS(name) VALUES (?)", ratingName) == 1;
    }

    /**
     * Удалить рейтинг с id = ratingId
     *
     * @param ratingId - id удаляемого рейтинга
     * @return true - если рейтинг был успешно удален
     */
    public boolean deleteRating(int ratingId) {
        return jdbcTemplate.update("DELETE FROM RATINGS WHERE rating_id = ?", ratingId) == 1;
    }
}
