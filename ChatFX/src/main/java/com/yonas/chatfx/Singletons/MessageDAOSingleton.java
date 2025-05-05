package com.yonas.chatfx.Singletons;


import com.yonas.chatfx.DAOs.Impl.MessageDAO;
import com.yonas.chatfx.DAOs.Interfaces.IMessageDAO;

public class MessageDAOSingleton {
    private static final IMessageDAO messageDAOInstance = new MessageDAO();

    public static IMessageDAO getMessageDAO() {
        return messageDAOInstance;
    }
}
