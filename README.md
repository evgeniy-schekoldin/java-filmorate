# java-filmorate
## Схема БД:
![](https://github.com/evgeniy-schekoldin/java-filmorate/blob/add-database/db.png)

### Примеры запросов к БД:

#### Получение топ N фильмов
```
SELECT f.name,
       COUNT(fl.film_id)
FROM films AS f
LEFT JOIN film_likes AS fl ON f.id=fl.film_id
GROUP BY f.name
ORDER BY COUNT(fl.film_id) DESC
LIMIT N
```

#### Получение списка друзей пользователя по его id
```
SELECT u.name
FROM friends_relations AS fr
LEFT JOIN users as u ON u.id=fr.friend_id
WHERE fr.user_id='1'
```

#### Получение списка общих друзей двух пользователей по их id
```
SELECT u.name
FROM friends_relations AS fr
LEFT JOIN users as u ON fr.friend_id=u.id
WHERE fr.user_id='x' OR fr.user_id='y'
GROUP BY u.name
HAVING COUNT(u.name) > 1
```
