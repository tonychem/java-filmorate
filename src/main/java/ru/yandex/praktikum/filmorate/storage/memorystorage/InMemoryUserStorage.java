package ru.yandex.praktikum.filmorate.storage.memorystorage;

import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {

    private long currentId = 1;
    private Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> users() {
        return users.values();
    }

    @Override
    public User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(currentId);
        users.put(currentId, user);
        currentId++;
        return user;
    }

    @Override
    public boolean deleteUser(long id) {
        User userToBeDeleted = users.remove(id);
        return userToBeDeleted != null;
    }


    @Override
    public User updateUser(User user) {
        Optional<User> userToBeUpdated = users.values().stream()
                .filter(x -> x.getId() == user.getId())
                .findAny();

        Optional<Long> id = userToBeUpdated.map(User::getId).or(() -> Optional.empty());

        if (id.isPresent()) {
            user.setId(id.get());
            users.put(id.get(), user);
        } else {
            return null;
        }

        return user;
    }
}
