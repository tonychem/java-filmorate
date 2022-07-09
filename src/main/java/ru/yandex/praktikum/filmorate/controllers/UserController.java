package ru.yandex.praktikum.filmorate.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.service.inmemoryservice.InMemoryUserService;
import ru.yandex.praktikum.filmorate.storage.UserStorage;
import ru.yandex.praktikum.filmorate.validation.Validator;

import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Set;

@RestController
@Slf4j
@Validated
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {
    private final Validator validator;
    private final UserStorage userStorage;
    private final InMemoryUserService inMemoryUserService;

    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.users();
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable @Positive long id) {
        return userStorage.userById(id);
    }

    @GetMapping(value = "/{id}/friends")
    public Set<User> getFriendListOfUser(@PathVariable @Positive long id) {
        return inMemoryUserService.friendList(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public Set<User> getMutualFriends(@PathVariable @Positive long id,
                                      @PathVariable @Positive long otherId) {
        return inMemoryUserService.mutualFriendList(id, otherId);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Positive long id,
                          @PathVariable @Positive long friendId) {
        inMemoryUserService.befriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFromFriendList(@PathVariable @Positive long id,
                                     @PathVariable @Positive long friendId) {
        inMemoryUserService.deleteFriend(id, friendId);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        validator.validateRequestBody(user);
        User userIdUpdated = userStorage.addUser(user);
        log.info("Пользователь {} был добавлен", userIdUpdated.getLogin());
        return userIdUpdated;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validator.validateRequestBody(user);
        User userToBeUpdated = userStorage.updateUser(user);
        log.info("Информация о пользователе {} была обновлена", userToBeUpdated.getLogin());
        return userToBeUpdated;
    }
}
