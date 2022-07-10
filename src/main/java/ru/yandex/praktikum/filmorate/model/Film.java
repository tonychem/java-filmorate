package ru.yandex.praktikum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String releaseDate;

    @Min(value = 1)
    private int duration;

    @EqualsAndHashCode.Exclude
    @Nullable
    private List<Genre> genres;

    @EqualsAndHashCode.Exclude
    @NonNull
    private MPA mpa;
}
