package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.filmorate.exception.NoSuchRatingException;
import ru.yandex.praktikum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
        if (rs.next()) {
            return rs.getString(1);
        } else {
            throw new RuntimeException(); //TODO: придумать что-нибудь получше
        }
    }

    public String getRatingName(int ratingId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT name FROM ratings WHERE rating_id = ?", ratingId);
        if (rowSet.next()) {
            return rowSet.getString(1);
        } else {
            throw new NoSuchRatingException(String.format("Рейтинга с id = %d не существует", ratingId)); //TODO: создать кастомную!
        }
    }

    public List<MPA> listOfRatingsInTable() {
        RowMapper<MPA> rmMPA = (rs, rowNum) -> {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            return new MPA(id, name);
        };

        List<MPA> listOfMPA = jdbcTemplate.query("SELECT * FROM ratings ORDER BY rating_id", rmMPA);

        return listOfMPA.subList(1, listOfMPA.size());
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
