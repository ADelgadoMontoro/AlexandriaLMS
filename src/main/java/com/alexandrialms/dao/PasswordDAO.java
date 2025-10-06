package com.alexandrialms.dao;

import com.alexandrialms.dao.interfaces.PasswordDAOInterface;
import com.alexandrialms.model.Password;
import com.alexandrialms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordDAO implements PasswordDAOInterface {
    @Override
    public boolean insert(Password password) {
        String sql = "INSERT INTO passwords (user_id, password_hash, created_at, updated_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, password.getUserID());
            pstm.setString(2, password.getPasswordHash());

            if (password.getCreatedAt() != null) {
                pstm.setTimestamp(3, Timestamp.valueOf(password.getCreatedAt()));
            } else {
                pstm.setNull(3, Types.TIMESTAMP);
            }

            if (password.getUpdatedAt() != null) {
                pstm.setTimestamp(4, Timestamp.valueOf(password.getUpdatedAt()));
            } else {
                pstm.setNull(4, Types.TIMESTAMP);
            }

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Password password) {
        String sql = "UPDATE passwords SET user_id = ?, password_hash = ?, created_at = ?, updated_at = ? WHERE password_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, password.getUserID());
            pstm.setString(2, password.getPasswordHash());

            if (password.getCreatedAt() != null) {
                pstm.setTimestamp(3, Timestamp.valueOf(password.getCreatedAt()));
            } else {
                pstm.setNull(3, Types.TIMESTAMP);
            }

            if (password.getUpdatedAt() != null) {
                pstm.setTimestamp(4, Timestamp.valueOf(password.getUpdatedAt()));
            } else {
                pstm.setNull(4, Types.TIMESTAMP);
            }

            pstm.setInt(5, password.getPasswordID());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer passwordID) {
        String sql = "DELETE FROM passwords WHERE password_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, passwordID);
            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Password findById(Integer passwordID) {
        String sql = "SELECT * FROM passwords WHERE password_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, passwordID);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Password> findAll() {
        List<Password> passwords = new ArrayList<>();
        String sql = "SELECT * FROM passwords";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                passwords.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwords;
    }

    private Password mapResultSet(ResultSet rs) throws SQLException {
        Password password = new Password();
        password.setPasswordID(rs.getInt("password_id"));
        password.setUserID(rs.getInt("user_id"));
        password.setPasswordHash(rs.getString("password_hash"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            password.setCreatedAt(created.toLocalDateTime());
        }

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            password.setUpdatedAt(updated.toLocalDateTime());
        }

        return password;
    }

    @Override
    public Password findByUserID(int userID) {
        String sql = "SELECT password_id, user_id, password_hash, created_at, updated_at FROM passwords WHERE user_id = ? ORDER BY created_at DESC LIMIT 1;";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Password> getPasswordHistory(int userID){
        List<Password> history = new ArrayList<>();
        String sql = "SELECT password_id, user_id, password_hash, created_at, updated_at FROM passwords WHERE user_id = ? ORDER BY created_at DESC;";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    history.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }

}
