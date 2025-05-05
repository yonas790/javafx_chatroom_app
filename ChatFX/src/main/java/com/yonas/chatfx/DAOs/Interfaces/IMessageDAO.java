package com.yonas.chatfx.DAOs.Interfaces;

import com.yonas.chatfx.Entities.Message;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface IMessageDAO extends IBaseDAO<Message>{
    ObservableList<Message> getChatMessages(int chatId) throws SQLException;
    int getChatId(int userId, int contactId) throws SQLException;
    int getUnreadMessageCount(int userId) throws SQLException;
    void updateReadMessages(int contactId, int chatId) throws SQLException;
    void insertMessage(int userId, String text, int chatId) throws SQLException;
    void createChat(int userId, int contactId) throws SQLException;
}
