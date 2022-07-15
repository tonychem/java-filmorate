package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.filmorate.exception.NoSuchRatingException;
import ru.yandex.praktikum.filmorate.model.MPA;

import java.util.List;

/**
 * DAO-класс для работы с таблицей RATINGS
 */
@Repository
@RequiredArgsConstructor
public class RatingsDAO {
    private final JdbcTemplate jdbcTemplate;

    public MPA rating(int ratingId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT name FROM ratings WHERE rating_id = ?", ratingId);
        if (rowSet.next()) {
            return new MPA(ratingId, rowSet.getString(1));
        } else {
            throw new NoSuchRatingException(String.format("Рейтинга с id = %d не существует", ratingId));
        }
    }

    public List<MPA> listOfRatingsInTable() {
        RowMapper<MPA> rmMPA = (rs, rowNum) -> {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            return new MPA(id, name);
        };

        List<MPA> listOfMPA = jdbcTemplate.query("SELECT * FROM ratings ORDER BY rating_id", rmMPA);

        return listOfMPA;
    }
}
