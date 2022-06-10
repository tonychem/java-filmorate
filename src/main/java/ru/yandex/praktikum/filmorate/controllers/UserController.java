package ru.yandex.praktikum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.UserStorage;
import ru.yandex.praktikum.filmorate.validation.ValidationException;
import ru.yandex.praktikum.filmorate.validation.Validator;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping(value = "/users")
public class UserController {
    private final Validator validator;
    private final UserStorage userStorage;

    public UserController(Validator validator, UserStorage userStorage) {
        this.validator = validator;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.users();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        try {
            validator.validateRequestBody(user);
        } catch (ValidationException e) {
            log.warn(e.getMessage() + "\n" + user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User userIdUpdated = userStorage.addUser(user);
        log.info("Пользователь {} был добавлен", userIdUpdated.getLogin());

        return userIdUpdated;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        try {
            validator.validateRequestBody(user);
        } catch (ValidationException e) {
            log.warn(e.getMessage() + "\n" + user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User userToBeUpdated = userStorage.updateUser(user);

        if (userToBeUpdated != null) {
            log.info("Информация о пользователе {} была обновлена", userToBeUpdated.getLogin());
            return userToBeUpdated;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
