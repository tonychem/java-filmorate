package ru.yandex.praktikum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.praktikum.filmorate.model.MPA;
import ru.yandex.praktikum.filmorate.storage.dbstorage.RatingsDAO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/mpa")
@AllArgsConstructor
public class RatingsController {

    private final RatingsDAO ratingsDAO;

    @GetMapping
    public List<MPA> ratings() {
        return ratingsDAO.listOfRatingsInTable();
    }

    @GetMapping(value = "/{id}")
    public MPA genreRatingById(@PathVariable int id) {
        return ratingsDAO.rating(id);
    }
}
