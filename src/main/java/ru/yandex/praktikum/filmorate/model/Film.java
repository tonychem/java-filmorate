package ru.yandex.praktikum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Film {
    @EqualsAndHashCode.Exclude
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
