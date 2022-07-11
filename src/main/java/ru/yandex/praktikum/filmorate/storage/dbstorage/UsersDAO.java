package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.filmorate.exception.NoSuchUserException;
import ru.yandex.praktikum.filmorate.exception.UserAlreadyExistsException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.Collection;

@Repository(value = "UserDBStorage")
@RequiredArgsConstructor
public class UsersDAO implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, row) -> {
        long userId = rs.getLong(1);
        String userLogin = rs.getString(2);
        String userName = rs.getString(3);
        String userEmail = rs.getString(4);
        String userBirthday = rs.getString(5);
        return new User(userId, userEmail, userLogin, userName, userBirthday);
    };

    @Override
    public Collection<User> users() {
        return jdbcTemplate.query("SELECT * FROM USERS", userRowMapper);
    }

    @Override
    public User userById(long id) {
        User user;
        try {
            user = jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE user_id = ?", userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchUserException(String.format("Пользователя с id = %d не существует", id));
        }
        return user;
    }

    @Override
    public User addUser(User user) {
        if (checkUserExists(user)) {
            throw new UserAlreadyExistsException(String.format("Пользователь с логином %s уже существует", user.getLogin()));
        } else {
            jdbcTemplate.update("INSERT INTO USERS(login, name, email, birthday) VALUES (?, ?, ?, ?)",
                    user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
            long assignedId = jdbcTemplate.queryForObject("SELECT user_id FROM USERS WHERE login = ?", long.class, user.getLogin());
            user.setId(assignedId);
            return user;
        }
    }

    @Override
    public boolean deleteUser(long id) {
        return jdbcTemplate.update("DELETE FROM USERS WHERE user_id = ?", id) != 0;
    }

    @Override
    public User updateUser(User user) {
        if (checkUserExists(user)) {
            jdbcTemplate.update("UPDATE USERS SET login = ?, name = ?, email = ?, birthday = ? WHERE user_id = ?",
                    user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
            return user;
        } else {
            throw new NoSuchUserException(String.format("Пользователя с id = %d не существует", user.getId()));
        }
    }

    public boolean checkUserExists(User user) {
        long userId = user.getId();
        SqlRowSet rs;
        int count = 0;

        if (userId == 0) {
            // если это новый пользователь, то узнаем, существует ли такой пользователь уже в базе по полям name, login, email
            rs = jdbcTemplate.queryForRowSet("SELECT COUNT(*) FROM users WHERE name = ? AND login = ? and email = ?",
                    user.getName(), user.getLogin(), user.getEmail());
        } else {
            //если пользователь приходит с id (для обновления), то проверяем по id
            rs = jdbcTemplate.queryForRowSet("SELECT COUNT(*) FROM users WHERE user_id = ?",
                    userId);
        }

        if (rs.next()) {
            count = rs.getInt(1);
        }

        return count == 1;
    }
}
