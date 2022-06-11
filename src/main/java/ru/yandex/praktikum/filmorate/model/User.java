package ru.yandex.praktikum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    private long id;

    @NonNull
    @Email
    private String email;

    @NonNull
    @NotBlank
    private String login;

    @NonNull
    private String name;

    @NonNull
    private String birthday;

}
