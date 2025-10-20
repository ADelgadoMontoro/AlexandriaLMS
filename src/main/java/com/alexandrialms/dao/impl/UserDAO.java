package com.alexandrialms.dao.impl;

import com.alexandrialms.model.User;
import com.alexandrialms.dao.interfaces.UserDAOInterface;
import com.alexandrialms.model.LibraryRole;
import com.alexandrialms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO implements UserDAOInterface {

    public boolean insert(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, phone, address, registration_date, role, active) "
                +
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
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, registration_date = ?, role = ?, active = ? "
                +
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

    @Override
    public User findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);
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
    public boolean delete(Integer id) throws SQLException {
        return delete(id.intValue());
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, email);
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
    public List<User> findByRole(String role) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, role);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> findActiveUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE active = TRUE";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> searchUsersByName(String name) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE first_name LIKE ? OR last_name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {
            String searchPattern = "%" + name + "%";
            pstm.setString(1, searchPattern);
            pstm.setString(2, searchPattern);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> findByRegistrationDateRange(Date startDate, Date endDate) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE registration_date BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setTimestamp(1, new Timestamp(startDate.getTime()));
            pstm.setTimestamp(2, new Timestamp(endDate.getTime()));

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> findAllPaginated(int limit, int offset) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, limit);
            pstm.setInt(2, offset);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> findByRolePaginated(String role, int limit, int offset) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ? LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, role);
            pstm.setInt(2, limit);
            pstm.setInt(3, offset);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> searchUsersByNamePaginated(String name, int limit, int offset) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE first_name LIKE ? OR last_name LIKE ? LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {
            String searchPattern = "%" + name + "%";
            pstm.setString(1, searchPattern);
            pstm.setString(2, searchPattern);
            pstm.setInt(3, limit);
            pstm.setInt(4, offset);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public int countUsersByRole(String role) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE role = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, role);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int countActiveUsers() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE active = TRUE";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int countUsersRegisteredInPeriod(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE registration_date BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setDate(1, startDate);
            pstm.setDate(2, endDate);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<User> findUsersWithActiveLoans() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN loans l ON u.user_id = l.user_id WHERE l.return_date IS NULL";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> findUsersWithOverdueLoans() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN loans l ON u.user_id = l.user_id WHERE l.due_date < CURRENT_DATE AND l.return_date IS NULL";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public int countActiveLoansByUser(Integer userId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM loans WHERE user_id = ? AND return_date IS NULL";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByPhone(String phone) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE phone = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, phone);
            ResultSet rs = pstm.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int countAllUsers() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean deactivateUser(Integer userId) throws SQLException {
        String sql = "UPDATE users SET active = 0 WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, userId);
            int rowsAffected = pstm.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean activateUser(Integer userId) throws SQLException {
        String sql = "UPDATE users SET active = 1 WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, userId);
            int rowsAffected = pstm.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public int deactivateInactiveUsers() throws SQLException {
        String sql = "UPDATE users SET active = 0 WHERE last_login < NOW() - INTERVAL 1 YEAR";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            int rowsAffected = pstm.executeUpdate();
            return rowsAffected;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteInactiveUsers() throws SQLException {
        String sql = "DELETE FROM users WHERE active = 0";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            return pstm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateUserRole(List<Integer> userIds, String newRole) throws SQLException {
        String sql = "UPDATE users SET role = ? WHERE user_id = ?";
        int totalUpdated = 0;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            for (Integer userId : userIds) {
                pstm.setString(1, newRole);
                pstm.setInt(2, userId);
                totalUpdated += pstm.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalUpdated;
    }

    @Override
    public int bulkDeactivateUsers(List<Integer> userIds) throws SQLException {
        String sql = "UPDATE users SET active = 0 WHERE user_id = ?";
        int totalUpdated = 0;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            for (Integer userId : userIds) {
                pstm.setInt(1, userId);
                totalUpdated += pstm.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalUpdated;
    }

    @Override
    public Map<String, Integer> getUsersCountByAllRoles() throws SQLException {
        Map<String, Integer> roleCounts = new HashMap<>();
        String sql = "SELECT role, COUNT(*) as count FROM users WHERE active = true GROUP BY role";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                String role = rs.getString("role");
                int count = rs.getInt("count");
                roleCounts.put(role, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        for (LibraryRole role : LibraryRole.values()) {
            roleCounts.putIfAbsent(role.name(), 0);
        }

        return roleCounts;
    }

    @Override
    public Map<Integer, Integer> getRegistrationsByMonth(int year) throws SQLException {
        Map<Integer, Integer> monthlyRegistrations = new HashMap<>();
        String sql = "SELECT MONTH(registration_date) as month, COUNT(*) as count " +
                "FROM users " +
                "WHERE YEAR(registration_date) = ? AND active = true " +
                "GROUP BY MONTH(registration_date)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, year);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    int count = rs.getInt("count");
                    monthlyRegistrations.put(month, count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        for (int month = 1; month <= 12; month++) {
            monthlyRegistrations.putIfAbsent(month, 0);
        }

        return monthlyRegistrations;
    }
}