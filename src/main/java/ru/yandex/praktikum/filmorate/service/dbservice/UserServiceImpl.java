package ru.yandex.praktikum.filmorate.service.dbservice;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.service.UserService;
import ru.yandex.praktikum.filmorate.storage.UserStorage;
import ru.yandex.praktikum.filmorate.storage.dbstorage.FriendshipDAO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository(value = "UserServiceDB")
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FriendshipDAO friendshipDAO;

    public UserServiceImpl(@Qualifier(value = "UserDBStorage") UserStorage userStorage,
                           FriendshipDAO friendshipDAO) {
        this.userStorage = userStorage;
        this.friendshipDAO = friendshipDAO;
    }

    @Override
    public void befriend(long sender, long acceptor) {
        if (friendshipDAO.checkFriendRequest(acceptor, sender)) {
            //если запрос на дружбу от acceptor к sender уже существует, тогда принять запрос в друзья
            friendshipDAO.acceptFriendRequest(acceptor, sender);
        } else {
            friendshipDAO.makeFriendRequest(sender, acceptor);
        }
    }

    @Override
    public void deleteFriend(long user, long friend) {
        friendshipDAO.declineFriendRequest(user, friend);
    }

    @Override
    public Set<User> friendList(long userId) {
        List<Long> userIds = friendshipDAO.friendsForUser(userId);
        return userIds.stream().map(userStorage::userById).collect(Collectors.toSet());
    }

    @Override
    public Set<User> mutualFriendList(long firstUser, long secondUser) {
        List<Long> firstUserFriends = friendshipDAO.friendsForUser(firstUser);
        List<Long> secondUserFriends = friendshipDAO.friendsForUser(secondUser);

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(userStorage::userById)
                .collect(Collectors.toSet());
    }
}
