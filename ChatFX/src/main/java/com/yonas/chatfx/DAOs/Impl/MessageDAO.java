package com.yonas.chatfx.DAOs.Impl;

import com.yonas.chatfx.DAOs.Interfaces.IMessageDAO;
import com.yonas.chatfx.Entities.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.yonas.chatfx.Connection.ConnectionManagement.conn;

public class MessageDAO implements IMessageDAO {
    /**
     * Method to retrieve all messages from a chat based on the chat number.
     * Preconditions: None
     * Postconditions: None
     * @param chatId
     * @return
     * @throws SQLException
     */
    @Override
    public ObservableList<Message> getChatMessages(int chatId) throws SQLException {
        ObservableList<Message> messageList = FXCollections.observableArrayList();

        if (chatId <= 0) {
            throw new IllegalArgumentException("Invalid chatId: " + chatId);
        }

        String sql = "SELECT Messages.UserId, Users.Username, Messages.Text, Messages.Date, Messages.Readed "
                + "FROM Messages INNER JOIN Users ON Messages.UserId = Users.UserId WHERE ChatId = ? ORDER BY Date;";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, chatId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Message message = new Message();
                    message.setIdUser(resultSet.getInt("UserId"));
                    message.setUserName(resultSet.getString("Username"));
                    message.setText(resultSet.getString("Text"));
                    message.setDate(resultSet.getTimestamp("Date"));
                    message.setReaded(resultSet.getBoolean("Readed"));
                    messageList.add(message);
                }
            }
        } catch (SQLException e) {
            throw e;
        }

        return messageList;
    }
    /**
     * Method to retrieve the chat number between a user and a contact based on their user IDs.
     * Preconditions: None
     * Postconditions: None
     * @param userId
     * @param contactId
     * @return
     * @throws SQLException
     */
    @Override
    public int getChatId(int userId, int contactId) throws SQLException {
        int chatId = -1;
        if (userId <= 0 || contactId <= 0) {
            throw new IllegalArgumentException("Invalid userId or contactId.");
        }
        String sql = "SELECT UM.ChatId FROM Users_Messages AS UM INNER JOIN Users_Messages AS MU ON UM.ChatId = MU.ChatId WHERE (UM.UserId = ?) AND (MU.UserId = ?);";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, contactId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    chatId = resultSet.getInt("ChatId");
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return chatId;
    }
    /**
     * Method to retrieve the number of unread messages for a user.
     * Preconditions: None
     * Postconditions: None
     * @param userId
     * @return
     * @throws SQLException
     */
    @Override
    public int getUnreadMessageCount(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId.");
        }
        int unreadCount = 0;
        String sql = "SELECT COUNT(MessageId) FROM Messages WHERE NOT UserId = ? AND Readed = 0 AND ChatId IN (SELECT ChatId FROM Users_Messages WHERE UserId = ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    unreadCount = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return unreadCount;
    }
    /**
     * Method to update the "Read" field in the database for the messages that have been read, based on the provided contactId and chatId.
     * Preconditions: None
     * Postconditions: None
     * @param contactId
     * @param chatId
     * @throws SQLException
     */
    @Override
    public void updateReadMessages(int contactId, int chatId) throws SQLException {
        if (contactId <= 0 || chatId <= 0) {
            throw new IllegalArgumentException("Invalid contactId or chatId.");
        }
        String sql = "UPDATE Messages SET Readed = 1 WHERE ChatId = ? AND UserId = ?;";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, chatId);
            statement.setInt(2, contactId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Method to insert a message into the database by providing the text, the userId of the sender, and the chatId to which it will be sent.
     * Preconditions: The message text cannot exceed 5000 characters.
     * Postconditions: None
     * @param userId
     * @param text
     * @param chatId
     * @throws SQLException
     */
    @Override
    public void insertMessage(int userId, String text, int chatId) throws SQLException {
        if (userId <= 0 || chatId <= 0 || text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Invalid userId, chatId, or text.");
        }
        String sql = "INSERT INTO Messages (UserId, ChatId, Text, Date, Readed) VALUES (?, ?, ?, CURRENT_TIMESTAMP, 0);";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, chatId);
            statement.setString(3, text);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    /**
     * Method to create a chat between a userId and a contactId.
     * Preconditions: None
     * Postconditions: A chat will be created between the userId and contactId with an ID greater than the last existing chat ID in the database.
     * @param userId
     * @param contactId
     * @throws SQLException
     */
    public void createChat(int userId, int contactId) throws SQLException {
        if (userId <= 0 || contactId <= 0) {
            throw new IllegalArgumentException("Invalid userId or contactId.");
        }
        int lastChatId = 0;
        // Use a transaction to ensure all operations are completed together or rolled back in case of an error.
        conn.setAutoCommit(false);
        try {
            // Query to retrieve the last chat ID in the database, if any. If no chat exists, set lastChatId = 1.
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT MAX(ChatId) AS MaxChatId FROM Users_Messages")) {
                if (resultSet.next()) {
                    lastChatId = resultSet.getInt("MaxChatId") + 1;
                } else {
                    lastChatId = 1;
                }
            }
            // Insert userId into the user-message relationship table with the chat ID.
            try (PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO Users_Messages (ChatId, UserId) VALUES (?, ?);")) {
                prepStatement.setInt(1, lastChatId);
                prepStatement.setInt(2, userId);
                prepStatement.executeUpdate();
            }
            // Insert contactId into the user-message relationship table with the chat ID.
            try (PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO Users_Messages (ChatId, UserId) VALUES (?, ?);")) {
                prepStatement.setInt(1, lastChatId);
                prepStatement.setInt(2, contactId);
                prepStatement.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            // Roll back the transaction in case of an error.
            conn.rollback();
            throw e;
        } finally {
            // Restore the auto-commit mode and close the connection.
            conn.setAutoCommit(true);
        }
    }

    @Override
    public void create(Message entity) {

    }

    @Override
    public void update(Message entity) {

    }

    @Override
    public void delete(Message entity) {

    }

    @Override
    public Message getById(int id) {
        return null;
    }

    @Override
    public List<Message> getAll() {
        return null;
    }
}
