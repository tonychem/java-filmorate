package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.filmorate.exception.NoSuchGenreException;
import ru.yandex.praktikum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * DAO-класс для работы с таблицей GENRES и соединительной таблицей FILM_GENRES
 */
@Repository
@RequiredArgsConstructor
public class GenresDAO {
    private final JdbcTemplate jdbcTemplate;

    public Genre genreById(int genreId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT name FROM GENRES WHERE genre_id = ?", genreId);
        if (rs.next()) {
            return new Genre(genreId, rs.getString(1));
        } else {
            throw new NoSuchGenreException(String.format("Жанра с id = %d не существует", genreId));
        }
    }

    public Set<Genre> setOfGenresForFilm(long filmId) {
        Set<Genre> genresForAFilm = new LinkedHashSet<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT genre_id FROM FILM_GENRES WHERE film_id = ? " +
                "ORDER BY genre_id", filmId);

        while (rs.next()) {
            genresForAFilm.add(genreById(rs.getInt(1)));
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

        return genreList;
    }

    public void addFilmGenres(long filmId, Set<Genre> listOfGenres) {
        for (Genre genre : listOfGenres) {
            jdbcTemplate.update("INSERT INTO FILM_GENRES(film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
        }
    }

    public void updateGenresOfFilm(long filmId, Set<Genre> filmGenres) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
        for (Genre genre : filmGenres) {
            jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
        }
    }

    public void deleteFilmFromFilmGenres(long filmId) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
    }
}
