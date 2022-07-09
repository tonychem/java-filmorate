package ru.yandex.praktikum.filmorate.model;

import lombok.*;
import org.springframework.lang.Nullable;

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
    private String releaseDate;

    @NonNull
    private int duration;

    @EqualsAndHashCode.Exclude
    @Nullable
    private List<Integer> genres;

    @EqualsAndHashCode.Exclude
    private int rating;
}
