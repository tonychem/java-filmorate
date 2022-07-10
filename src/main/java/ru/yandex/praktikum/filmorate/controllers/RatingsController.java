package ru.yandex.praktikum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.praktikum.filmorate.storage.dbstorage.RatingsDAO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/mpa")
@AllArgsConstructor
public class RatingsController {

    private final RatingsDAO ratingsDAO;

    @GetMapping
    public Map<Integer, String> ratings() {
        return ratingsDAO.listOfRatingsInTable();
    }

    @GetMapping(value = "/{id}")
    public Map<String, Object> genreRatingById(@PathVariable(name = "id") int id) {
        String name = ratingsDAO.getRatingName(id);
        HashMap<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
        //TODO: лучше через объекты
    }
}
