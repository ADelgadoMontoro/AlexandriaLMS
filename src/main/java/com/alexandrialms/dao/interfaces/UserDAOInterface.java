package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAOInterface extends GenericDAO<User, Integer> {
    User findByEmail(String email) throws SQLException;
    List<User> findByRole(String role) throws SQLException;
    List<User> findActiveUsers() throws SQLException;
}
