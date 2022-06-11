package ru.yandex.praktikum.filmorate.service;

import org.springframework.stereotype.Service;
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

    public void befriend(User user1, User user2) {
        long user1Id = user1.getId();
        long user2Id = user2.getId();

        if (!friendMap.containsKey(user1Id)) {
            friendMap.put(user1Id, new HashSet<>());
        }

        if (!friendMap.containsKey(user2Id)) {
            friendMap.put(user2Id, new HashSet<>());
        }

        friendMap.get(user1Id).add(user2Id);
        friendMap.get(user2Id).add(user1Id);
    }

    public void deleteFriend(User user, User friend) {
        Set<Long> friendSet = friendMap.get(user.getId());
        if (friendSet != null) {
            friendSet.remove(friend);
        } else {
            throw new FriendListEmptyException(String.format("Список друзей пользователя %s пуст", user.getName()));
        }
    }

    public Set<User> mutualFriendList(User user1, User user2) {
        Set<Long> user1FriendList = friendMap.get(user1);
        Set<Long> user2FriendList = friendMap.get(user2);

        //В user1FriendList оставляем только те элементы, которые содержаться в user2FriendList
        user1FriendList.retainAll(user2FriendList);

        //Отображаем Set<Long> -> Set<User>
        return user1FriendList.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toSet());
    }


}
