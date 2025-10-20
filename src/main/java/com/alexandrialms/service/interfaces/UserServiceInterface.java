package com.alexandrialms.service.interfaces;

import com.alexandrialms.model.User;
import com.alexandrialms.exception.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserServiceInterface {
    
    // CRUD OPERATIONS
    Optional<User> createUser(User user) throws ValidationException;
    Optional<User> getUserById(int userId) throws ValidationException;
    boolean updateUser(int userId, User user) throws ValidationException;
    boolean deleteUser(int userId) throws ValidationException;
    
    // BASIC SEARCH OPERATIONS
    List<User> getAllUsers();
    List<User> searchUsers(String searchTerm) throws ValidationException;
    List<User> getUsersByName(String name) throws ValidationException;
    Optional<User> getUserByEmail(String email) throws ValidationException;
    List<User> getUsersByRole(String role) throws ValidationException;
    List<User> getActiveUsers();
    
    // ADVANCED SEARCH OPERATIONS
    List<User> getUsersByRegistrationDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) throws ValidationException;
    List<User> getUsersByNameAndRole(String name, String role) throws ValidationException;
    
    // PAGINATION OPERATIONS
    List<User> getUsersByNamePaginated(String name, int limit, int offset) throws ValidationException;
    List<User> getUsersByRolePaginated(String role, int limit, int offset) throws ValidationException;
    List<User> getAllUsersPaginated(int limit, int offset) throws ValidationException;
    
    // STATISTICS OPERATIONS
    int getUsersCountByRole(String role) throws ValidationException;
    int getActiveUsersCount();
    int getUsersRegisteredInPeriod(java.time.LocalDate startDate, java.time.LocalDate endDate) throws ValidationException;
    Map<String, Integer> getUsersCountByRole() throws ValidationException;
    Map<Integer, Integer> getUsersRegistrationByMonth(int year) throws ValidationException;
    int getTotalUsersCount();
    
    // LOAN-RELATED OPERATIONS
    List<User> getUsersWithActiveLoans() throws ValidationException;
    List<User> getUsersWithOverdueLoans() throws ValidationException;
    int getActiveLoansCountByUser(int userId) throws ValidationException;
    boolean canUserBorrowMore(int userId) throws ValidationException;
    int getUserLoanLimit(int userId) throws ValidationException;
    
    // AVAILABILITY & STATUS OPERATIONS
    boolean isUserActive(int userId) throws ValidationException;
    boolean deactivateUser(int userId) throws ValidationException;
    boolean activateUser(int userId) throws ValidationException;

    
    // VALIDATION OPERATIONS
    boolean userExistsByEmail(String email);
    boolean userExistsByPhone(String phone);
    boolean userHasActiveLoans(int userId) throws ValidationException;
    boolean isUserEligibleForLoan(int userId) throws ValidationException;
    
    // SECURITY & ACCESS OPERATIONS
    //boolean validateUserCredentials(String email, String password) throws ValidationException;
    //boolean changeUserPassword(int userId, String newPassword) throws ValidationException;
    //boolean resetUserPassword(int userId) throws ValidationException;
    
    // BATCH OPERATIONS
    int deactivateInactiveUsers() throws ValidationException;
    int updateUsersRole(List<Integer> userIds, String newRole) throws ValidationException;
    int bulkDeactivateUsers(List<Integer> userIds) throws ValidationException;
    int notifyUsersWithOverdueLoans() throws ValidationException;
    
    // MAINTENANCE OPERATIONS
    int cleanupInactiveUsers() throws ValidationException;
    //List<User> getUsersRequiringRenewal() throws ValidationException;
    
    // REPORTING OPERATIONS
    Map<String, Object> getUserStatistics(int userId) throws ValidationException;
    List<Map<String, Object>> getUsersLoanActivityReport() throws ValidationException;
    List<Map<String, Object>> getUsersRegistrationReport(int year) throws ValidationException;
}