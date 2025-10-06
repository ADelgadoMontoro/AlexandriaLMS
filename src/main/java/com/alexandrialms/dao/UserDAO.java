package com.alexandrialms.dao;

import com.alexandrialms.model.User;
import com.alexandrialms.model.LibraryRole;
import com.alexandrialms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean insert(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, phone, address, registration_date, role, active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, user.getFirstName());
            pstm.setString(2, user.getLastName());
            pstm.setString(3, user.getEmail());
            pstm.setString(4, user.getPhone());
            pstm.setString(5, user.getAddress());

            // registrationDate como Timestamp
            if (user.getRegistrationDate() != null) {
                pstm.setTimestamp(6, Timestamp.valueOf(user.getRegistrationDate()));
            } else {
                pstm.setNull(6, Types.TIMESTAMP);
            }

            // Enum guardado como string
            pstm.setString(7, user.getRole().name());
            pstm.setBoolean(8, user.isActive());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, registration_date = ?, role = ?, active = ? " +
                     "WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, user.getFirstName());
            pstm.setString(2, user.getLastName());
            pstm.setString(3, user.getEmail());
            pstm.setString(4, user.getPhone());
            pstm.setString(5, user.getAddress());

            if (user.getRegistrationDate() != null) {
                pstm.setTimestamp(6, Timestamp.valueOf(user.getRegistrationDate()));
            } else {
                pstm.setNull(6, Types.TIMESTAMP);
            }

            pstm.setString(7, user.getRole().name());
            pstm.setBoolean(8, user.isActive());
            pstm.setInt(9, user.getUserID());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int userID) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, userID);
            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User findById(int userID) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, userID);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User mapResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));

        Timestamp ts = rs.getTimestamp("registration_date");
        if (ts != null) {
            user.setRegistrationDate(ts.toLocalDateTime());
        }

        String roleStr = rs.getString("role");
        if (roleStr != null) {
            user.setRole(LibraryRole.valueOf(roleStr));
        }

        user.setActive(rs.getBoolean("active"));

        return user;
    }
}