package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface UserDAOInterface extends GenericDAO<User, Integer> {

    // BASIC SEARCH METHODS
    User findByEmail(String email) throws SQLException;

    List<User> findByRole(String role) throws SQLException;

    List<User> findActiveUsers() throws SQLException;

    // ADVANCED SEARCH METHODS
    List<User> searchUsersByName(String name) throws SQLException;

    List<User> findByRegistrationDateRange(java.sql.Date startDate, java.sql.Date endDate) throws SQLException;

    // PAGINATION METHODS
    List<User> findAllPaginated(int limit, int offset) throws SQLException;

    List<User> findByRolePaginated(String role, int limit, int offset) throws SQLException;

    List<User> searchUsersByNamePaginated(String name, int limit, int offset) throws SQLException;

    // STATISTICS METHODS
    int countUsersByRole(String role) throws SQLException;

    int countActiveUsers() throws SQLException;

    int countUsersRegisteredInPeriod(java.sql.Date startDate, java.sql.Date endDate) throws SQLException;

    // LOAN-RELATED METHODS (REQUIRES JOIN WITH LOAN TABLE)
    List<User> findUsersWithActiveLoans() throws SQLException;

    List<User> findUsersWithOverdueLoans() throws SQLException;

    int countActiveLoansByUser(Integer userId) throws SQLException;

    // VALIDATION AND EXISTENCE METHODS
    boolean existsByEmail(String email) throws SQLException;

    boolean existsByPhone(String phone) throws SQLException;

    int countAllUsers() throws SQLException;

    // MAINTENANCE OPERATIONS
    boolean deactivateUser(Integer userId) throws SQLException;

    boolean activateUser(Integer userId) throws SQLException;

    int deleteInactiveUsers() throws SQLException;

    // BATCH OPERATIONS
    int updateUserRole(List<Integer> userIds, String newRole) throws SQLException;

    int bulkDeactivateUsers(List<Integer> userIds) throws SQLException;

    Map<String, Integer> getUsersCountByAllRoles() throws SQLException;

    Map<Integer, Integer> getRegistrationsByMonth(int year) throws SQLException;
}