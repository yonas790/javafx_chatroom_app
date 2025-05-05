package com.yonas.chatfx.Singletons;

import com.yonas.chatfx.DAOs.Impl.UserDAO;
import com.yonas.chatfx.DAOs.Interfaces.IUserDAO;

public class UserDAOSingleton {
    private static final IUserDAO userDAOInstance = new UserDAO();
    public static IUserDAO getUserDAO() {
        return userDAOInstance;
    }
}
