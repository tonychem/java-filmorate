package ru.yandex.praktikum.filmorate.storage.memorystorage;

import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.filmorate.exception.NoSuchUserException;
import ru.yandex.praktikum.filmorate.exception.UserAlreadyExistsException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository(value = "InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private long currentId = 1;
    private Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> users() {
        return users.values();
    }

    @Override
    public User userById(long id) {
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        if (users.values().contains(user)) {
            throw new UserAlreadyExistsException(String.format("Пользователь с логином %s уже существует", user.getLogin()));
        } else {
            user.setId(currentId);
            users.put(currentId, user);
            currentId++;
            return user;
        }
    }

    @Override
    public boolean deleteUser(long id) {
        return users.remove(id) != null;
    }


    @Override
    public User updateUser(User user) {
        long id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
            return user;
        } else {
            throw new NoSuchUserException(String.format("Пользователя с id = %d не существует", id));
        }
    }
}
