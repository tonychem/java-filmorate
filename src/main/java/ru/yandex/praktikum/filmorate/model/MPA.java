package ru.yandex.praktikum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MPA {
    private int id;
    private String name;
    @JsonCreator
    public MPA(@JsonProperty("id") int id,
               @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
