package ru.yandex.praktikum.filmorate.service;

import ru.yandex.praktikum.filmorate.model.User;

import java.util.Set;

public interface UserService {
    void befriend(long firstUser, long secondUser);
    void deleteFriend(long user, long friend);
    Set<User> friendList(long userId);
    Set<User> mutualFriendList(long firstUser, long secondUser);

}
