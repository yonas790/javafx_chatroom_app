package com.yonas.chatfx.Services.Impl;

import com.yonas.chatfx.Services.Interfaces.IChatService;
import com.yonas.chatfx.Singletons.MessageDAOSingleton;
import com.yonas.chatfx.DAOs.Interfaces.IContactDAO;
import com.yonas.chatfx.DAOs.Interfaces.IMessageDAO;
import com.yonas.chatfx.Entities.Contact;
import com.yonas.chatfx.Entities.Message;
import com.yonas.chatfx.Singletons.ContactDAOSingleton;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ChatService implements IChatService {
    private final IContactDAO contactDAO;
    private final IMessageDAO messageDAO;

    public ChatService() {
        this.contactDAO = ContactDAOSingleton.getContactDAO();
        this.messageDAO = MessageDAOSingleton.getMessageDAO();
    }

    @Override
    public int getUnreadMessageCount(int userId) throws SQLException {
        return messageDAO.getUnreadMessageCount(userId);
    }

    @Override
    public ObservableList<Contact> getUserContacts(int userId) throws SQLException {
        return contactDAO.getUserContacts(userId);
    }

    @Override
    public ObservableList<Message> getChatMessages(int userId, int contactId) throws SQLException {

        return messageDAO.getChatMessages(messageDAO.getChatId(userId,contactId));
    }

    @Override
    public void updateReadMessages( int contactId, int userId) throws SQLException {

        messageDAO.updateReadMessages(contactId,messageDAO.getChatId(userId,contactId));
    }

    @Override
    public void addContact(int userId, int contactId) throws SQLException {
        contactDAO.addContact(userId,contactId);
    }

    @Override
    public void createChat(int userId, int contactId) throws SQLException {
        messageDAO.createChat(userId,contactId);
    }

    @Override
    public void blockUnblockContact(int userId, int contactId, boolean isBlocked) throws SQLException {
        contactDAO.blockUnblockContact(userId,contactId,isBlocked);
    }

    @Override
    public void insertMessage(int userId, String text, int contactId) throws SQLException {
       int chatId = messageDAO.getChatId(userId,contactId);
        if(!contactDAO.checkBlocked(userId,chatId)){
        messageDAO.insertMessage(userId,text,chatId);
        }
    }
}
