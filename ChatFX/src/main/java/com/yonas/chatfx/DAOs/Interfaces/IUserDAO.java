package com.yonas.chatfx.DAOs.Interfaces;

import com.yonas.chatfx.Entities.User;

import java.sql.SQLException;

public interface IUserDAO extends IBaseDAO<User>{
    boolean checkUserExistence(User user) throws SQLException;
    User getUserId(User user) throws SQLException;
}
