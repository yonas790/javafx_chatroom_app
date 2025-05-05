package com.yonas.chatfx.DAOs.Impl;

import com.yonas.chatfx.Connection.ConnectionManagement;
import com.yonas.chatfx.DAOs.Interfaces.IUserDAO;
import com.yonas.chatfx.Entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO implements IUserDAO {
    /**
     * Method to check the existence of a user in the database based on the provided user data.
     * Preconditions: None
     * Postconditions: None
     * @param user
     * @return boolean
     */
    @Override
    public boolean checkUserExistence(User user) throws SQLException {
        boolean UserExists = false;
        if (user == null || user.getUserName() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User and its properties cannot be null.");
        }
        String sql = "SELECT COUNT(UserId) FROM Users WHERE Username = ? AND Password = ?";
        try (PreparedStatement statement = ConnectionManagement.conn.prepareStatement(sql)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    UserExists = count > 0;
                }
            }
        }
        return UserExists;
    }
    /**
     * Method to retrieve the user ID based on the provided user data.
     * Preconditions: None
     * Postconditions: None
     * @param user
     * @return
     * @throws SQLException
     */
    @Override
    public User getUserId(User user) throws SQLException {
        User userLoad = new User(-1,user.getUserName(), user.getPassword());
        if (user == null || user.getUserName() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User and its properties cannot be null.");
        }
        String sql = "SELECT UserId FROM Users WHERE Username = ? AND Password = ?";
        try (PreparedStatement statement = ConnectionManagement.conn.prepareStatement(sql)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userLoad.setIdUser(resultSet.getInt("UserId"));
                }
            }
        }
        return userLoad;
    }
    /**
     * Method to insert a user into the database with the provided username and password.
     * Preconditions: Usernames are unique, so there cannot be users with the same username. The username is not a primary key and does not need to be inherited by other tables.
     * @param entity
     * @throws SQLException
     */
    @Override
    public void create(User entity) throws SQLException {
        if (entity == null || entity.getUserName() == null || entity.getPassword() == null) {
            throw new IllegalArgumentException("User and its properties cannot be null.");
        }
        String sql = "INSERT INTO Users (Username, Password) VALUES (?, ?);";
        try (PreparedStatement statement = ConnectionManagement.conn.prepareStatement(sql)) {
            statement.setString(1, entity.getUserName());
            statement.setString(2, entity.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public User getById(int id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
