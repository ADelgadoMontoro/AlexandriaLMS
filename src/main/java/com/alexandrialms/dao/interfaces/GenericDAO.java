package com.alexandrialms.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T, ID> {

    List<T> findAll() throws SQLException;

    T findById(ID id) throws SQLException;

    boolean insert(T entity) throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean delete(ID id) throws SQLException;
}

