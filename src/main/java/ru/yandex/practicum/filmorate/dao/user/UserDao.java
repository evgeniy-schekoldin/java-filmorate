package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {

    User addUser(User user);

    User updateUser(User user);

    User getUser(long id);

    List<User> getUsers();

    void addFriend(long id, long friendId);

    void removeFriend(long id, long friendId);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long id, long friendId);

}
