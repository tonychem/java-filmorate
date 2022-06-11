package ru.yandex.praktikum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.praktikum.filmorate.exception.NoSuchUserException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //Отображение Id пользователя на множество Id его друзей\
    private final Map<Long, Set<Long>> friendMap = new HashMap<>();

    public void befriend(long user1Id, long user2Id) {
        checkUserIdentity(user1Id, user2Id);
        checkUserExists(user1Id);
        checkUserExists(user2Id);

        if (!friendMap.containsKey(user1Id)) {
            friendMap.put(user1Id, new HashSet<>());
        }

        if (!friendMap.containsKey(user2Id)) {
            friendMap.put(user2Id, new HashSet<>());
        }

        friendMap.get(user1Id).add(user2Id);
        friendMap.get(user2Id).add(user1Id);

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
                    .map(userStorage::getUserById)
                    .collect(Collectors.toSet());
        } else {
            checkUserExists(userId);
        }
        return Collections.emptySet();
    }

    public Set<User> mutualFriendList(long user1Id, long user2Id) {
        checkUserExists(user1Id);
        checkUserExists(user2Id);

        if (friendMap.get(user1Id) == null || friendMap.get(user2Id) == null) {
            return Collections.emptySet();
        }

        Set<Long> user1FriendList = new HashSet<>(friendMap.get(user1Id));
        Set<Long> user2FriendList = new HashSet<>(friendMap.get(user2Id));

        //В user1FriendList оставляем только те элементы, которые содержаться в user2FriendList
        user1FriendList.retainAll(user2FriendList);

        //Отображаем Set<Long> -> Set<User>
        return user1FriendList.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toSet());
    }

    private void checkUserExists(long userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NoSuchUserException(String.format("Пользователя с id = %d не существует", userId));
        }
    }

    private void checkUserIdentity(long user1Id, long user2Id) {
        if (user1Id == user2Id) {
            throw new IllegalStateException("Нельзя совершать операции добавления в друзья " +
                    "/ удаление из друзей над самим собой");
        }
    }

}
