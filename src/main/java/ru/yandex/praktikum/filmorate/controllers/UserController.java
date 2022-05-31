package ru.yandex.praktikum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.validation.ValidationException;
import ru.yandex.praktikum.filmorate.validation.Validator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value = "/users")
public class UserController {
    private Map<Long, User> users = new HashMap<>();
    private final Validator validator;

    private long currentId = 1;

    public UserController(Validator validator) {
        this.validator = validator;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        try {
            validator.validateRequestBody(user);
        } catch (ValidationException e) {
            log.warn(e.getMessage() + "\n" + user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // Приходящему пользователю устанавливается id
        user.setId(currentId);

        users.put(user.getId(), user);
        currentId++;
        log.info("Пользователь {} был добавлен", user.getLogin());

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        try {
            validator.validateRequestBody(user);
        } catch (ValidationException e) {
            log.warn(e.getMessage() + "\n" + user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // Проверяем, есть ли в таблице пользователь с таким же id
        Optional<User> userToBeUpdated = users.values().stream()
                .filter(x -> x.getId() == user.getId())
                .findAny();
        //Если в таблице находится пользователь с таким id, то извлекаем данный id, иначе бросаем NOT_FOUND
        long id = userToBeUpdated.map(User::getId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        user.setId(id);
        users.put(id, user);
        log.info("Информация о пользователе {} была обновлена", user.getLogin());

        return user;
    }
}
