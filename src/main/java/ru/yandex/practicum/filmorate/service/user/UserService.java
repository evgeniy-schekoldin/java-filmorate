package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User GetUserById(long id) throws UserNotFoundException {
        return userStorage.getUser(id);
    }

    public void addFriend(long id, long friendId) {
        userStorage.getUser(id).addFriend(userStorage.getUser(friendId).getId());
        userStorage.getUser(friendId).addFriend(id);
        log.info("Добавление в друзья id={}, friendId={}", id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        if (userStorage.getUser(id).getFriends().contains(friendId)) {
            userStorage.getUser(id).removeFriend(friendId);
            userStorage.getUser(friendId).removeFriend(id);
            log.info("Удаление из друзей id={}, friendId={}", id, friendId);
        }
    }

    public List<User> getFriends(long id) {
        Set<Long> friendsIds = userStorage.getUser(id).getFriends();
        return friendsIds.stream().map(userStorage::getUser).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        Set<Long> friends = userStorage.getUser(id).getFriends();
        Set<Long> otherFriends = userStorage.getUser(otherId).getFriends();
        List<Long> commonFriends = friends.stream().filter(otherFriends::contains).collect(Collectors.toList());
        return commonFriends.stream().map(userStorage::getUser).collect(Collectors.toList());
    }

}
