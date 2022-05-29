package ru.yandex.praktikum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Film {
    private long id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    private String description;

    @NonNull
    private String releaseDate;

    @NonNull
    private int duration;
}
