package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.praktikum.filmorate.exception.NoSuchFilmException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.model.Genre;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Set;

/**
 * DAO-класс для работы с таблицей FILMS
 */
@Repository(value = "FilmDBStorage")
@AllArgsConstructor
public class FilmsDAO implements FilmStorage {
    private JdbcTemplate jdbcTemplate;
    private GenresDAO genresDAO;
    private RatingsDAO ratingsDAO;

    private final RowMapper<Film> filmRowMapper = (rs, row) -> {
        long filmId = rs.getLong(1);
        String filmName = rs.getString(2);
        String filmDescription = rs.getString(3);
        String filmReleaseDate = rs.getString(4);
        int filmDuration = rs.getInt(5);
        Set<Genre> filmGenres = genresDAO.setOfGenresForFilm(filmId);
        int ratingId = rs.getInt(6);
        return new Film(filmId, filmName, filmDescription, filmReleaseDate,
                filmDuration, filmGenres, ratingsDAO.rating(ratingId));
    };

    @Override
    public Film filmById(long id) {
        Film film;
        try {
            film = jdbcTemplate.queryForObject("SELECT * FROM FILMS WHERE film_id = ?", filmRowMapper, id);
        } catch (EmptyResultDataAccessException exc) {
            throw new NoSuchFilmException(String.format("Фильм с id = %d отсутствует", id));
        }
        return film;
    }

    @Override
    public Collection<Film> films() {
        return jdbcTemplate.query("SELECT * FROM FILMS", filmRowMapper);
    }

    @Override
    public Film addFilm(Film film) {
        if (films().contains(film)) {
            throw new FilmAlreadyExistsException(String.format("Фильм %s уже существует", film.getName()));
        } else {
            //Добавляем фильм в таблицу FILMS
            jdbcTemplate.update("INSERT INTO FILMS (name, description, releasedate, duration, rating_id) VALUES (?, ?, ?, ?, ?)",
                    film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());

            long assignedId = jdbcTemplate.queryForObject("SELECT film_id FROM FILMS WHERE name = ?", long.class,
                    film.getName());

            //Список жанров вставляем в таблицу FILM_GENRES
            if (film.getGenres() != null) {
                genresDAO.addFilmGenres(assignedId, film.getGenres());
            }

            film.setId(assignedId);

            return film;
        }
    }

    @Override
    public boolean deleteFilm(long id) {
        return jdbcTemplate.update("DELETE FROM FILMS WHERE film_id = ?", id) != 0;
    }

    @Override
    public Film updateFilm(Film film) {
        long id = film.getId();
        if (checkFilmExists(id)) {
            //обновить таблицу FILMS
            jdbcTemplate.update("UPDATE FILMS SET name = ?, description = ?, releasedate = ?, duration = ?, rating_id = ? " +
                            "WHERE film_id = ?",
                    film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), id);
            //обновить таблицу FILM_GENRES
            if (film.getGenres() != null) {
                genresDAO.updateGenresOfFilm(film.getId(), film.getGenres());
            }
            return filmById(id);
        } else {
            throw new NoSuchFilmException(String.format("Фильм с id = %d отсутствует", id));
        }
    }

    public boolean checkFilmExists(long filmId) {
        return filmById(filmId) != null;
    }
}
