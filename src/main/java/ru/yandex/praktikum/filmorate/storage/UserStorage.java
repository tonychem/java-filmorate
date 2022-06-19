package ru.yandex.praktikum.filmorate.storage;

import ru.yandex.praktikum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> users();

    User userById(long id);

    User addUser(User user);

    boolean deleteUser(long id);

    User updateUser(User user);
}
