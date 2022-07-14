package ru.yandex.praktikum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.praktikum.filmorate.model.Genre;
import ru.yandex.praktikum.filmorate.storage.dbstorage.GenresDAO;

import java.util.List;

@RestController
@RequestMapping(value = "/genres")
@AllArgsConstructor
public class GenresController {

    private final GenresDAO genresDAO;

    @GetMapping
    public List<Genre> genres() {
        return genresDAO.listOfGenresInTable();
    }

    @GetMapping(value = "/{id}")
    public Genre genreById(@PathVariable int id) {
        return genresDAO.genreById(id);
    }

}
