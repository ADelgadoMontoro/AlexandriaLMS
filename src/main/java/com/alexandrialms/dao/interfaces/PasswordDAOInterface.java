package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.Password;
import java.sql.SQLException;
import java.util.List;

public interface PasswordDAOInterface extends GenericDAO<Password, Integer> {
    Password findByUserID(int userID) throws SQLException;
    List<Password> getPasswordHistory(int userID) throws SQLException;
}
