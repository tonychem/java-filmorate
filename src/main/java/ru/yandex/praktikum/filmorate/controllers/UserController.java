package ru.yandex.praktikum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.exception.NoSuchUserException;
import ru.yandex.praktikum.filmorate.exception.UserAlreadyExistsException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.service.UserService;
import ru.yandex.praktikum.filmorate.storage.UserStorage;
import ru.yandex.praktikum.filmorate.validation.Validator;

import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Set;

@RestController
@Slf4j
@Validated
@RequestMapping(value = "/users")
public class UserController {
    private final Validator validator;
    private final UserStorage userStorage;
    private final UserService userService;

    public UserController(Validator validator, UserStorage userStorage, UserService userService) {
        this.validator = validator;
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.users();
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable @Positive long id) {
        return userStorage.getUserById(id);
    }

    @GetMapping(value = "/{id}/friends")
    public Set<User> getFriendListOfUser(@PathVariable @Positive long id) {
        return userService.friendList(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public Set<User> getMutualFriends(@PathVariable @Positive long id,
                                      @PathVariable @Positive long otherId) {
        return userService.mutualFriendList(id, otherId);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Positive long id,
                          @PathVariable @Positive long friendId) {
        userService.befriend(id, friendId);
    }
    
    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFromFriendList(@PathVariable @Positive long id,
                                     @PathVariable @Positive long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (validator.validateRequestBody(user)) {
            User userIdUpdated = userStorage.addUser(user);
            log.info("Пользователь {} был добавлен", userIdUpdated.getLogin());

            return userIdUpdated;
        } else {
            throw new UserAlreadyExistsException(String.format("Пользователь с логином %s уже существует",
                    user.getLogin()));
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validator.validateRequestBody(user);

        User userToBeUpdated = userStorage.updateUser(user);

        if (userToBeUpdated != null) {
            log.info("Информация о пользователе {} была обновлена", userToBeUpdated.getLogin());
            return userToBeUpdated;
        } else {
            throw new NoSuchUserException(String.format("Пользователь с id = %d отсутствует", user.getId()));
        }
    }
}
