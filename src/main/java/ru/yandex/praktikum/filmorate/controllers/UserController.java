package ru.yandex.praktikum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.validation.ValidationException;
import ru.yandex.praktikum.filmorate.validation.Validator;

import javax.validation.Valid;
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
    public Map<Long, User> getUsers() {
        return users;
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

        // Считаем, что обновляемый и новый юзер должны иметь хотя бы одинаковый email
        Optional<User> userToBeUpdated = users.values().stream()
                .filter(x -> x.getEmail().equals(user.getEmail()))
                .findAny();
        //Если в таблице находится пользователь с таким id, то извлекаем данный id, иначе присваиваем текущий id
        long id = userToBeUpdated.map(User::getId).orElseGet(() -> currentId++);

        //устанавливаем id пользователя и его имя, если оно пусто
        user.setId(id);
        users.put(id, user);
        log.info("Информация о пользователе {} была обновлена", user.getLogin());

        return user;
    }
}
