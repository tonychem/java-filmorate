# Filmorate project
![Entity-relationship diagram](/.github/workflows/erd.png)

1. Основные сущности базы данных - фильм (**film**) и пользователь (**user**). 
2. Связь между фильмом и пользователями, которые его лайкнули, задается в соединительной таблице **film_likes**. Аналогично дружба между пользователями описана через соединительную таблицу **friendship**, содержащую поле *accepted*. Предполагается, что при запросе дружбы от одного пользователя к другому в базе будет создаваться две записи с ключами ```(user1, user2)``` и ```(user2, user1)```, в которых поле *accepted* будет задавать "направление" дружбы. Например, запись ```user1_id, user2_id, true``` означает, что user1 отправил запрос на дружбу user2. Если в базе существует запись ```user2_id, user1_id, true``` значит, что user1 и user2 являются друзьями, иначе - нет (односторонняя дружба). Также гарантируется, что в базе нет пары таких взаимных ключей, поле *accepted* которых равно ```false``` (иначе - если заявка в друзья отклонена, два поля с взаимными ключами удаляются).
3. Вывести всех пользователей, которые лайкнули фильм с id = 1.
```sql
SELECT u.name
FROM users u
WHERE u.user_id IN (
	SELECT fl.user_id
	FROM film_likes fl
	WHERE fl.film_id = 1
)
```
4. Вывести топ 10 фильмов.
```sql
SELECT f.name
FROM films f
INNER JOIN (
	SELECT fl.film_id, COUNT(user_id) likes
	FROM film_likes fl
	GROUP BY fl.film_id
	ORDER BY likes DESC
	LIMIT 10
) like_table ON f.filmd_id = like_table.film_id
```
5. Найти общих друзей пользователя id = 1 и id = 2
```sql
SELECT name
FROM users
WHERE user_id IN (
	SELECT first_user_friendlist.userTwo_id --вывести id общего друга
	FROM (
		SELECT userOne_id, userTwo_id
		FROM friendship
		WHERE userOne_id = 1 AND userTwo_id != 2

	) first_user_friendlist -- Таблица с друзьями пользователя id = 1 (кроме 2)

	INNER JOIN (
		SELECT userOne_id, userTwo_id
		FROM friendship
		WHERE userOne_id = 2 AND userTwo_id != 1
	) second_user_friendlist -- Объединяется с таблицей с друзьями пользователя id = 2 (кроме 1) по общим друзьям
	ON first_user_friendlist.userTwo_id = second_user_friendlist.userTwo_id AND first_user_friendlist.status = second_user_friendlist.status
)
```
