package ru.yandex.praktikum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.filmorate.exception.NoSuchUserException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    //Отображение Id пользователя на множество Id его друзей\
    private final Map<Long, Set<Long>> friendMap = new HashMap<>();

    public void befriend(long firstUser, long secondUser) {
        checkUserIdentity(firstUser, secondUser);
        checkUserExists(firstUser);
        checkUserExists(secondUser);

        if (!friendMap.containsKey(firstUser)) {
            friendMap.put(firstUser, new HashSet<>());
        }

        if (!friendMap.containsKey(secondUser)) {
            friendMap.put(secondUser, new HashSet<>());
        }

        friendMap.get(firstUser).add(secondUser);
        friendMap.get(secondUser).add(firstUser);

    }

    public void deleteFriend(long user, long friend) {
        checkUserIdentity(user, friend);

        Set<Long> friendSet = friendMap.get(user);
        if (friendSet != null) {
            friendSet.remove(friend);
        } else {
            checkUserExists(user);
        }
    }

    public Set<User> friendList(long userId) {
        Set<Long> friendSet = friendMap.get(userId);
        if (friendSet != null) {
            return friendSet.stream()
                    .map(userStorage::userById)
                    .collect(Collectors.toSet());
        } else {
            checkUserExists(userId);
        }
        return Collections.emptySet();
    }

    public Set<User> mutualFriendList(long firstUser, long secondUser) {
        checkUserExists(firstUser);
        checkUserExists(secondUser);

        if (friendMap.get(firstUser) == null || friendMap.get(secondUser) == null) {
            return Collections.emptySet();
        }

        Set<Long> user1FriendList = new HashSet<>(friendMap.get(firstUser));
        Set<Long> user2FriendList = new HashSet<>(friendMap.get(secondUser));

        //В user1FriendList оставляем только те элементы, которые содержаться в user2FriendList
        user1FriendList.retainAll(user2FriendList);

        //Отображаем Set<Long> -> Set<User>
        return user1FriendList.stream()
                .map(userStorage::userById)
                .collect(Collectors.toSet());
    }

    private void checkUserExists(long userId) {
        if (userStorage.userById(userId) == null) {
            throw new NoSuchUserException(String.format("Пользователя с id = %d не существует", userId));
        }
    }

    private void checkUserIdentity(long firstUser, long secondUser) {
        if (firstUser == secondUser) {
            throw new IllegalStateException("Нельзя совершать операции добавления в друзья " +
                    "/ удаление из друзей над самим собой");
        }
    }

}
