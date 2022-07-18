package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component("UserDaoImp")
public class UserDaoImpl implements UserDao {

    private final static String USERS_USER_ID_COLUMN = "user_id";
    private final static String USERS_USER_EMAIL_COLUMN = "email";
    private final static String USERS_USER_LOGIN_COLUMN = "login";
    private final static String USERS_USER_NAME_COLUMN = "user_name";
    private final static String USERS_USER_BIRTHDAY_COLUMN = "birthday";
    private final static String DATE_FORMATTER = "yyyy-MM-d";

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO users (email, login, user_name, birthday) " +
                     "VALUES (?,?,?,?)";
        KeyHolder userId = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sql, new String[]{USERS_USER_ID_COLUMN});
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getLogin());
                statement.setString(3, user.getName());
                statement.setString(4, user.getBirthday().toString());
                return statement;
            }, userId);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistException(user.getEmail());
        }

        user.setId(userId.getKey().longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users " +
                     "SET email=?, login=?, user_name=?, birthday=? " +
                     "WHERE user_id=?";
        int affectedRows = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (affectedRows > 0) return user;
        throw new UserNotFoundException(user.getId());
    }

    @Override
    public User getUser(long id) {
        String sql = "SELECT * " +
                     "FROM users " +
                     "WHERE user_id=?";
        List<User> result = jdbcTemplate.query(sql, this::makeUser, id);
        if (result.size() > 0) {
            return result.get(0);
        }
        throw new UserNotFoundException(id);
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * " +
                     "FROM users";
        return jdbcTemplate.query(sql, this::makeUser);
    }

    @Override
    public void addFriend(long id, long friendId) {
        getUser(id);
        getUser(friendId);
        String sql = "INSERT INTO user_friends (user_id, friend_id, accepted) " +
                     "VALUES (?,?,?)";
        jdbcTemplate.update(sql, id, friendId, true);
    }

    @Override
    public void removeFriend(long id, long friendId) {
        getUser(id);
        getUser(friendId);
        String sql = "DELETE FROM user_friends " +
                     "WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        String sql = "SELECT u.* FROM users AS u " +
                     "JOIN user_friends AS uf ON uf.friend_id = u.user_id " +
                     "WHERE uf.user_id=?";
        return jdbcTemplate.query(sql, this::makeUser, id);
    }

    @Override
    public List<User> getCommonFriends(long id, long friendId) {
        String sql = "SELECT u.* from user_friends as uf " +
                     "JOIN users as u ON uf.friend_id = u.user_id " +
                     "WHERE uf.user_id = ? or uf.user_id = ? " +
                     "GROUP BY u.user_id, u.email, u.login, u.user_name, u.birthday " +
                     "HAVING COUNT(uf.friend_id) > 1";
        return jdbcTemplate.query(sql, this::makeUser, id, friendId);
    }

    private User makeUser(ResultSet result, int row) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        User user = new User();
        user.setId(result.getLong(USERS_USER_ID_COLUMN));
        user.setEmail(result.getString(USERS_USER_EMAIL_COLUMN));
        user.setLogin(result.getString(USERS_USER_LOGIN_COLUMN));
        user.setName(result.getString(USERS_USER_NAME_COLUMN));
        user.setBirthday(LocalDate.parse(result.getString(USERS_USER_BIRTHDAY_COLUMN), formatter));
        return user;
    }

}