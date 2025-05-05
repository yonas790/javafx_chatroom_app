package com.yonas.chatfx.DAOs.Interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IBaseDAO<T> {
    void create(T entity) throws SQLException;
    void update(T entity);
    void delete(T entity);
    T getById(int id);
    List<T> getAll();
}
