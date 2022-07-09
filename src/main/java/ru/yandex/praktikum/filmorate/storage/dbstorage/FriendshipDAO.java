package ru.yandex.praktikum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO-класс для работы с таблицей Friendship
 */
@Repository
@RequiredArgsConstructor
public class FriendshipDAO {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Находит всех друзей пользователя с id userId
     *
     * @param userId - id пользователя, чьих друзей ищем
     * @return список id друзей пользователя
     */
    public List<Long> getAllFriendsForUser(long userId) {
        String sqlSubquery = "SELECT userTwo FROM FRIENDSHIP WHERE userone_id = ? AND accepted = true";
        String sqlQuery = String.format("SELECT userone_id from FRIENDSHIP WHERE userone_id IN (%s) and accepted = true",
                sqlSubquery);

        return jdbcTemplate.queryForList(sqlQuery, long.class, userId);
    }

    public void makeFriendRequest(long fromUserOne, long toUserTwo) {
        // вносится запись, что userOne отправил запрос на дружбу к userTwo
        jdbcTemplate.update("INSERT INTO FRIENDSHIP(userone_id, usertwo_id, accepted) VALUES (?, ?, true)",
                fromUserOne, toUserTwo);
        // симметрично вносится запись у userTwo
        jdbcTemplate.update("INSERT INTO FRIENDSHIP(userone_id, usertwo_id, accepted) VALUES (?, ?, true)",
                toUserTwo, fromUserOne);
    }

    public void acceptFriendRequest(long senderUserId, long acceptingUserId) {
        jdbcTemplate.update("UPDATE friendship SET accepted = true " +
                "WHERE userone_id = ? AND usertwo_id = ?", acceptingUserId, senderUserId);
    }

    public void declineFriendRequest(long senderUserId, long decliningUserId) {
        jdbcTemplate.update("DELETE FROM friendship WHERE userone_id = ? AND usertwo_id = ?",
                senderUserId, decliningUserId);
        jdbcTemplate.update("DELETE FROM friendship WHERE userone_id = ? AND usertwo_id = ?",
                decliningUserId, senderUserId);
    }

    //Проверяет, есть ли запрос на дружбу от senderUser к acceptingUser
    public boolean checkFriendRequest(long senderUser, long acceptingUser) {
        SqlRowSet rs1 = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDSHIP " +
                        "WHERE userone_id = ? AND usertwo_id = ? AND accepted = true",
                senderUser, acceptingUser);
        SqlRowSet rs2 = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDSHIP " +
                        "WHERE userone_id = ? AND usertwo_id = ? AND accepted = false",
                acceptingUser, senderUser);
        return rs1.next() && rs2.next();
    }


}
