package ru.yandex.praktikum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    private Validator validator = new Validator();

    private static long currentId = 1;

    @GetMapping
    public Map<Long, User> getUsers() {
        return users;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (validator.validateRequestBody(user)) {
            // Приходящему пользователю устанавливается id и имя, если оно пустое
            user.setId(currentId);
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }

            users.put(user.getId(), user);

            currentId++;
            log.info("Пользователь {} был добавлен", user.getLogin());
            return user;

        } else {
            String exceptionMessage = "Ошибка при добавлении пользователя";
            log.warn(exceptionMessage + "\n" + user);
            throw new ValidationException(exceptionMessage);
        }
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (validator.validateRequestBody(user)) {
            // Считаем, что обновляемый и новый юзер должны иметь хотя бы одинаковый email
            Optional<User> userToBeUpdated = users.values().stream()
                    .filter(x -> x.getEmail().equals(user.getEmail()))
                    .findAny();
            //Если в таблице находится пользователь с таким id, то извлекаем данный id, иначе присваиваем текущий id
            long id = userToBeUpdated.map(User::getId).orElseGet(() -> currentId++);

            //устанавливаем id пользователя и его имя, если оно пусто
            user.setId(id);
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }

            users.put(id, user);
            log.info("Информация о пользователе {} была обновлена", user.getLogin());
            return user;
        } else {
            String exceptionMessage = "Ошибка при обновлении данных пользователя";
            log.warn(exceptionMessage + "\n" + user);
            throw new ValidationException(exceptionMessage);
        }
    }
}
