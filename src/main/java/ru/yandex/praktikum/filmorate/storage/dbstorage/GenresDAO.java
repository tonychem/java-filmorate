package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.filmorate.model.Genre;

import java.util.*;

/**
 * DAO-класс для работы с таблицей GENRES и соединительной таблицей FILM_GENRES
 */
@Repository
@RequiredArgsConstructor
public class GenresDAO {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Извлечь название жанра по id
     *
     * @param genreId - id жанра
     * @return Название жанра
     */
    public String getGenreName(int genreId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT name FROM GENRES WHERE genre_id = ?", genreId);
        if (rs.next()) {
            return rs.getString(1);
        } else {
            throw new RuntimeException(); //TODO: new exception добавить везде!
        }
    }

    /**
     * Добавить новый жанр в таблицу
     *
     * @param genreName - название жанра
     * @return true - если жанр был добавлен
     */
    public boolean addGenre(String genreName) {
        return jdbcTemplate.update("INSERT INTO GENRES(name) VALUES (?)", genreName) == 1;
    }

    /**
     * Вернуть список id жанров для данного фильма
     *
     * @param filmId - id фильма
     * @return Список id жанров для данного фильма
     */
    public Set<Integer> listOfGenresForFilm(long filmId) {
        Set<Integer> genresForAFilm = new LinkedHashSet<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT genre_id FROM FILM_GENRES WHERE film_id = ? " +
                "ORDER BY genre_id", filmId);

        while (rs.next()) {
            genresForAFilm.add(rs.getInt(1));
        }
        return genresForAFilm;
    }

    public List<Genre> listOfGenresInTable() {
        RowMapper<Genre> rmGenre = (rs, rowNum) -> {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            return new Genre(id, name);
        };

        List<Genre> genreList = jdbcTemplate.query("SELECT * FROM GENRES ORDER BY genre_id", rmGenre);

        return genreList.subList(1, genreList.size());
    }

    /**
     * Добавить список жанров для фильма в таблицу FILM_GENRES
     *
     * @param filmId       - id фильма
     * @param listOfGenres - список жанров фильма
     * @return true - если все значения были добавлены в таблицу
     */
    public boolean addFilmGenres(long filmId, Set<Genre> listOfGenres) {
        int successCount = 0;
        for (Genre genre : listOfGenres) {
            successCount += jdbcTemplate.update("INSERT INTO FILM_GENRES(film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
        }
        return successCount == listOfGenres.size();
    }

    /**
     * Удаляет жанр из таблицы GENRES
     *
     * @param genreId - id жанра
     * @return true - если жанр блы удален
     */

    public boolean deleteGenre(int genreId) {
        return jdbcTemplate.update("DELETE FROM GENRES WHERE genre_id = ?", genreId) == 1;
    }

    public void deleteFilmFromFilmGenres(long filmId) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
    }
}
