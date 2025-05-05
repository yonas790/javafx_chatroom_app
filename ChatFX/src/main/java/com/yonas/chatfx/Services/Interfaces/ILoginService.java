package com.yonas.chatfx.Services.Interfaces;

import com.yonas.chatfx.Entities.User;

import java.sql.SQLException;

public interface ILoginService {
    User getUserId(User user) throws SQLException;
    void create(User entity) throws SQLException;
}
