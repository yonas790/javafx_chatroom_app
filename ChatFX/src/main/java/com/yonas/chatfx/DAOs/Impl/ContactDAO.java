package com.yonas.chatfx.DAOs.Impl;

import com.yonas.chatfx.Connection.ConnectionManagement;
import com.yonas.chatfx.DAOs.Interfaces.IContactDAO;
import com.yonas.chatfx.Entities.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.yonas.chatfx.Connection.ConnectionManagement.conn;

public class ContactDAO implements IContactDAO {
    /**
     * Method to retrieve all contacts of a user based on their user ID.
     */
    @Override
    public ObservableList<Contact> getUserContacts(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId.");
        }
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        String sql = "SELECT u.UserId, u.Username, c.Blocked FROM Users AS u "
                + "INNER JOIN Users_Contacts AS c ON c.ContactId = u.UserId "
                + "WHERE u.UserId IN (SELECT ContactId FROM Users_Contacts WHERE UserId = ?) AND c.UserId = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Contact contact = new Contact();
                    contact.setIdContact(resultSet.getInt("UserId"));
                    contact.setUserName(resultSet.getString("Username"));
                    contact.setBlocked(resultSet.getBoolean("Blocked"));
                    contactList.add(contact);
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return contactList;
    }
    /**
     * Method to check if a user is blocked in a chat based on their user ID and the chat number.
     * Preconditions: None
     * Postconditions: None
     * @param userId
     * @param chatId
     * @return
     * @throws SQLException
     */
    @Override
    public boolean checkBlocked(int userId, int chatId) throws SQLException {
        if (userId <= 0 || chatId <= 0) {
            throw new IllegalArgumentException("Invalid userId or chatId.");
        }
        boolean isBlocked;
        int blockedBit;
        String sql = "SELECT Blocked FROM Users_Contacts WHERE ContactId = ? AND UserId = (SELECT UserId FROM Users_Messages WHERE ChatId = ? AND UserId != ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, chatId);
            statement.setInt(3, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    blockedBit = resultSet.getInt("Blocked");
                    isBlocked = blockedBit != 0;
                } else {
                    // If there is no matching record, consider the user as not blocked.
                    isBlocked = false;
                }
            }
        } catch (SQLException e) {
            // Handle the exception appropriately (e.g., log it or throw a custom exception)
            throw e;
        }
        return isBlocked;
    }
    /**
     * Method to update the "Blocked" field in the database for a contact, indicating whether the contact is blocked or unblocked.
     * Preconditions: None
     * Postconditions: None
     * @param userId
     * @param contactId
     * @param isBlocked
     * @throws SQLException
     */
    @Override
    public  void blockUnblockContact(int userId, int contactId, boolean isBlocked) throws SQLException {
        if (userId <= 0 || contactId <= 0) {
            throw new IllegalArgumentException("Invalid userId or contactId.");
        }
        String sql = "UPDATE Users_Contacts SET Blocked = ? WHERE UserId = ? AND ContactId = ?;";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, isBlocked ? 1 : 0);
            statement.setInt(2, userId);
            statement.setInt(3, contactId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    /**
     * Method to add a contact by providing the userId and contactId.
     * Preconditions: None
     * Postconditions: Two relationships will be added: a - b and b - a
     * @param userId
     * @param contactId
     * @throws SQLException
     */
    @Override
    public void addContact(int userId, int contactId) throws SQLException {
        if (userId <= 0 || contactId <= 0) {
            throw new IllegalArgumentException("Invalid userId or contactId.");
        }

        String sql = "INSERT INTO Users_Contacts (ContactId, UserId, Blocked) VALUES (?, ?, 0);";
        try {
            // Use a transaction to ensure all operations are completed together or rolled back in case of an error.
            conn.setAutoCommit(false);
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                // Add the first contact for the user
                statement.setInt(1, contactId);
                statement.setInt(2, userId);
                statement.executeUpdate();

                // Add the second contact for the user
                statement.setInt(1, userId);
                statement.setInt(2, contactId);
                statement.executeUpdate();
            }

            // Commit the transaction if everything is successful
            conn.commit();
        } catch (SQLException e) {
            // Roll back the transaction in case of an error
            conn.rollback();
            throw e;
        } finally {
            // Restore the auto-commit mode and close the connection.
            conn.setAutoCommit(true);
        }
    }

    @Override
    public void create(Contact entity) {

    }

    @Override
    public void update(Contact entity) {

    }

    @Override
    public void delete(Contact entity) {

    }

    @Override
    public Contact getById(int id) {
        return null;
    }

    @Override
    public List<Contact> getAll() {
        return null;
    }
}
