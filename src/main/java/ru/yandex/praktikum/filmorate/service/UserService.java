package ru.yandex.praktikum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.praktikum.filmorate.exception.FriendListEmptyException;
import ru.yandex.praktikum.filmorate.exception.NoSuchUserException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //Отображение Id пользователя на множество Id его друзей
    private final Map<Long, Set<Long>> friendMap = new HashMap<>();

    public void befriend(long user1Id, long user2Id) {
        if (user1Id == user2Id) {
            throw new IllegalStateException("Нельзя добавить самого себя в друзья");
        }

        if (userStorage.getUserById(user1Id) == null) {
            throw new NoSuchUserException(String.format("Пользователя с id = %d не существует", user1Id));
        }

        if (userStorage.getUserById(user2Id) == null) {
            throw new NoSuchUserException(String.format("Пользователя с id = %d не существует", user2Id));
        }

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
        if (user == friend) {
            throw new IllegalStateException("Нельзя удалить самого себя из друзей");
        }

        Set<Long> friendSet = friendMap.get(user);
        if (friendSet != null) {
            friendSet.remove(friend);
        } else {
            checkUser(user);
        }
    }

    public Set<User> friendList(long userId) {
        Set<Long> friendSet = friendMap.get(userId);
        if (friendSet != null) {
            return friendSet.stream()
                    .map(userStorage::getUserById)
                    .collect(Collectors.toSet());
        } else {
            checkUser(userId);
        }
        return null;
    }
    public Set<User> mutualFriendList(long user1, long user2) {
        Set<Long> user1FriendList = friendMap.get(user1);
        Set<Long> user2FriendList = friendMap.get(user2);
        if (user1FriendList == null) {
            throw new FriendListEmptyException(String.format("У пользователя с id = %d список друзей пуст", user1));
        }

        if (user2FriendList == null) {
            throw new FriendListEmptyException(String.format("У пользователя с id = %d список друзей пуст", user2));
        }
        //В user1FriendList оставляем только те элементы, которые содержаться в user2FriendList
        user1FriendList.retainAll(user2FriendList);

        //Отображаем Set<Long> -> Set<User>
        return user1FriendList.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toSet());
    }

    //Метод выбрасывает соответствующее исключение, если пользователь отсутствует в UserStorage
    //или у пользователя отсутствует список друзей
    private void checkUser(long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NoSuchUserException(String.format("Пользователя с id = %d не существует", userId));
        } else {
            throw new FriendListEmptyException(String.format("У пользователя %s нет друзей",
                    user.getName()));
        }
    }
}
