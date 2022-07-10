package ru.yandex.praktikum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.praktikum.filmorate.model.Genre;
import ru.yandex.praktikum.filmorate.storage.dbstorage.GenresDAO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> genreById(@PathVariable(name = "id") int id) {
        String name = genresDAO.getGenreName(id);
        HashMap<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
        //TODO: лучше через объекты
    }

    //TODO: привести все к id-шникам, либо к типам Genre?
}
