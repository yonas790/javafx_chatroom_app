package com.yonas.chatfx.Services.Impl;

import com.yonas.chatfx.DAOs.Interfaces.IUserDAO;
import com.yonas.chatfx.Services.Interfaces.ILoginService;
import com.yonas.chatfx.Singletons.UserDAOSingleton;
import com.yonas.chatfx.Entities.User;

import java.sql.SQLException;

public class LoginService implements ILoginService {
    private final IUserDAO userDAO;

    public LoginService() {
        this.userDAO = UserDAOSingleton.getUserDAO();
    }

    @Override
    public User getUserId(User user) throws SQLException {
        return userDAO.getUserId(user);
    }

    @Override
    public void create(User entity) throws SQLException {
        userDAO.create(entity);
    }
}
