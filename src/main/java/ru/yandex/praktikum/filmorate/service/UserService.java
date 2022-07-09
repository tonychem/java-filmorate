package ru.yandex.praktikum.filmorate.service;

import ru.yandex.praktikum.filmorate.model.User;

import java.util.Set;

public interface UserService {
    public void befriend(long firstUser, long secondUser);
    public void deleteFriend(long user, long friend);
    public Set<User> friendList(long userId);
    public Set<User> mutualFriendList(long firstUser, long secondUser);

}
