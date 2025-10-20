package com.alexandrialms.service.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alexandrialms.dao.impl.UserDAO;
import com.alexandrialms.exception.ValidationException;
import com.alexandrialms.model.User;
import com.alexandrialms.service.interfaces.UserServiceInterface;
import com.alexandrialms.util.ValidationHelper;

public class UserServiceImpl implements UserServiceInterface {
    UserDAO userDAO = new UserDAO();

    @Override
    public Optional<User> createUser(User user) throws ValidationException {
        ValidationHelper.validateUserForInsert(user, userDAO);

        boolean success = userDAO.insert(user);
        if (success) {
            return Optional.of(user); 
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserById(int userId) throws ValidationException {
        ValidationHelper.validateUserId(userId);
        User user;
        try {
            user = userDAO.findById(userId);
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean updateUser(int userId, User user) throws ValidationException {
        if (user.getUserID() != userId) {
            throw new ValidationException("userId", "ID_MISMATCH",
                    "User ID in path (" + userId + ") does not match user object ID (" + user.getUserID() + ")");
        }

        ValidationHelper.validateUserForUpdate(user, userDAO);

        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(int userId) throws ValidationException {
        ValidationHelper.validateUserDeletion(userId, userDAO);

        return userDAO.delete(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public List<User> searchUsers(String searchTerm) throws ValidationException {
        ValidationHelper.validateUserSearchTerm(searchTerm);
        try {
            return userDAO.searchUsersByName(searchTerm);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getUsersByName(String name) throws ValidationException {
        ValidationHelper.validateUserSearchTerm(name);
        try {
            return userDAO.searchUsersByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws ValidationException {
        if (!ValidationHelper.isValidEmail(email)) {
            throw new ValidationException("email", "INVALID_EMAIL", "The provided email is not valid.");
        }
        try {
            User user = userDAO.findByEmail(email);
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<User> getUsersByRole(String role) throws ValidationException {
        ValidationHelper.validateUserRole(role);
        try {
            return userDAO.findByRole(role);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getActiveUsers() {
        try {
            return userDAO.findActiveUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getUsersByRegistrationDateRange(LocalDate startDate, LocalDate endDate)
            throws ValidationException {
        if (!ValidationHelper.isValidDateRange(startDate, endDate)) {
            throw new ValidationException("dateRange", "INVALID_DATE_RANGE",
                    "The provided date range is not valid. End date must be after start date and none of the dates can be in the future.");
        }

        try {
            return userDAO.findByRegistrationDateRange(Date.valueOf(startDate), Date.valueOf(endDate));
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getUsersByNameAndRole(String name, String role) throws ValidationException {
        ValidationHelper.validateUserSearchTerm(name);
        ValidationHelper.validateUserRole(role);

        try {
            List<User> usersByName = userDAO.searchUsersByName(name);
            return usersByName.stream()
                    .filter(user -> user.getRole().name().equalsIgnoreCase(role))
                    .collect(Collectors.toList());

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getUsersByNamePaginated(String name, int limit, int offset) throws ValidationException {
        ValidationHelper.validateUserSearchTerm(name);
        ValidationHelper.validatePaginationParameters(limit, offset);
        try {
            return userDAO.searchUsersByNamePaginated(name, limit, offset);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getUsersByRolePaginated(String role, int limit, int offset) throws ValidationException {
        ValidationHelper.validateUserRole(role);
        ValidationHelper.validatePaginationParameters(limit, offset);
        try {
            return userDAO.findByRolePaginated(role, limit, offset);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getAllUsersPaginated(int limit, int offset) throws ValidationException {
        ValidationHelper.validatePaginationParameters(limit, offset);
        try {
            return userDAO.findAllPaginated(limit, offset);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public int getUsersCountByRole(String role) throws ValidationException {
        ValidationHelper.validateUserRole(role);
        try {
            return userDAO.countUsersByRole(role);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getActiveUsersCount() {
        try {
            return userDAO.countActiveUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getUsersRegisteredInPeriod(LocalDate startDate, LocalDate endDate) throws ValidationException {
        if (!ValidationHelper.isValidDateRange(startDate, endDate)) {
            throw new ValidationException("dateRange", "INVALID_DATE_RANGE",
                    "The provided date range is not valid.");
        }
        try {
            return userDAO.countUsersRegisteredInPeriod(Date.valueOf(startDate), Date.valueOf(endDate));
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Map<String, Integer> getUsersCountByRole() throws ValidationException {
       try {
        return userDAO.getUsersCountByAllRoles();
       } catch (SQLException e) {
        e.printStackTrace();
        return new HashMap<>();
       }
    }

    @Override
    public Map<Integer, Integer> getUsersRegistrationByMonth(int year) throws ValidationException { 
        Map<Integer, Integer> registrationMap = new HashMap<>();
        try {
            registrationMap = userDAO.getRegistrationsByMonth(year);
            return registrationMap;
        } catch (SQLException e) {
            e.printStackTrace();
            return registrationMap;
        }
    }

    @Override
    public int getTotalUsersCount() {
        try {
            return userDAO.countAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<User> getUsersWithActiveLoans() throws ValidationException {
        List<User> usersWithLoans = new ArrayList<>();
        try {
            usersWithLoans = userDAO.findUsersWithActiveLoans();
            return usersWithLoans;
        } catch (SQLException e) {
            e.printStackTrace();
            return usersWithLoans;
        }
    }

    @Override
    public List<User> getUsersWithOverdueLoans() throws ValidationException {
        List<User> usersWithOverdueLoans = new ArrayList<>();
        try {
            usersWithOverdueLoans = userDAO.findUsersWithOverdueLoans();
            return usersWithOverdueLoans;
        } catch (SQLException e) {
            e.printStackTrace();
            return usersWithOverdueLoans;
        }
    }

    @Override
    public int getActiveLoansCountByUser(int userId) throws ValidationException {
        ValidationHelper.validateUserId(userId);
        try {
            return userDAO.countActiveLoansByUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean canUserBorrowMore(int userId) throws ValidationException {
        ValidationHelper.validateUserId(userId);
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new ValidationException("userId", "USER_NOT_FOUND", "User with ID " + userId + " not found.");
            }
            int activeLoans = userDAO.countActiveLoansByUser(userId);
            int loanLimit = user.getRole().getLoanLimit();
            return activeLoans < loanLimit;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getUserLoanLimit(int userId) throws ValidationException {
        ValidationHelper.validateUserId(userId);
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new ValidationException("userId", "USER_NOT_FOUND", "User with ID " + userId + " not found.");
            }
            return user.getRole().getLoanLimit();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean isUserActive(int userId) throws ValidationException {
        ValidationHelper.validateUserId(userId);
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new ValidationException("userId", "USER_NOT_FOUND", "User with ID " + userId + " not found.");
            }
            return user.isActive();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


@Override
public boolean deactivateUser(int userId) throws ValidationException {
    ValidationHelper.validateUserId(userId);
    
    if (getActiveLoansCountByUser(userId) > 0) {
        throw new ValidationException("userId", "USER_HAS_ACTIVE_LOANS",
                "Cannot deactivate user with active loans");
    }    
    try {
        return userDAO.deactivateUser(userId);
    } catch (SQLException e) {
        throw new ValidationException("userId", "DATABASE_ERROR",
                "Error deactivating user: " + e.getMessage());
    }
}

    @Override
    public boolean activateUser(int userId) throws ValidationException {
        ValidationHelper.validateUserId(userId);
        try {
            return userDAO.activateUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean userExistsByEmail(String email) {
        if (!ValidationHelper.isValidEmail(email)) {
            return false;
        }
        try {
            return userDAO.existsByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean userExistsByPhone(String phone) {
        if (!ValidationHelper.isValidPhone(phone)) {
            return false;
        }
        try {
            return userDAO.existsByPhone(phone);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean userHasActiveLoans(int userId) throws ValidationException {
        ValidationHelper.validateUserId(userId);
        try {
            int activeLoans = userDAO.countActiveLoansByUser(userId);
            return activeLoans > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isUserEligibleForLoan(int userId) throws ValidationException {
        ValidationHelper.validateUserId(userId);
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new ValidationException("userId", "USER_NOT_FOUND", "User with ID " + userId + " not found.");
            }
            int activeLoans = userDAO.countActiveLoansByUser(userId);
            int loanLimit = user.getRole().getLoanLimit();
            return activeLoans < loanLimit && user.isActive();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // TODO Security & Access Operations
  /*  @Override
    public boolean validateUserCredentials(String email, String password) throws ValidationException {
        if (!ValidationHelper.isValidEmail(email)) {
            throw new ValidationException("email", "INVALID_EMAIL", "The provided email is not valid.");
        }
        if (password == null || password.isEmpty()) {
            throw new ValidationException("password", "INVALID_PASSWORD", "The provided password is not valid.");
        }
        try {
            User user = userDAO.findByEmail(email);
            if (user == null) {
                return false;
            }
            return user.getPassword().equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changeUserPassword(int userId, String newPassword) throws ValidationException {
        ValidationHelper.validateUserId(userId);
        ValidationHelper.validatePassword(newPassword);
        try {
            return userDAO.updateUserPassword(userId, newPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resetUserPassword(int userId) throws ValidationException {
        // Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetUserPassword'");
    }
*/
    @Override
    public int deactivateInactiveUsers() throws ValidationException {
        try {
            return userDAO.deactivateInactiveUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ValidationException("deactivateInactiveUsers", "DATABASE_ERROR",
                    "Error deactivating inactive users: " + e.getMessage());
        }
    }

    @Override
    public int updateUsersRole(List<Integer> userIds, String newRole) throws ValidationException {
        ValidationHelper.validateUserRole(newRole);
        try {
            return userDAO.updateUserRole(userIds, newRole);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ValidationException("updateUsersRole", "DATABASE_ERROR",
                    "Error updating user roles: " + e.getMessage());
        }
    }

    @Override
    public int bulkDeactivateUsers(List<Integer> userIds) throws ValidationException {
        try {
            return userDAO.bulkDeactivateUsers(userIds);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ValidationException("bulkDeactivateUsers", "DATABASE_ERROR",
                    "Error bulk deactivating users: " + e.getMessage());
        }
    }

@Override
public int notifyUsersWithOverdueLoans() throws ValidationException {
    try {
        List<User> usersWithOverdueLoans = userDAO.findUsersWithOverdueLoans();
        
        int notifiedCount = 0;
        
        for (User user : usersWithOverdueLoans) {
            boolean notified = sendOverdueNotification(user);
            if (notified) {
                notifiedCount++;
            }
        }
        
        return notifiedCount;
        
    } catch (SQLException e) {
        throw new ValidationException("notifications", "DATABASE_ERROR",
                "Error retrieving users with overdue loans: " + e.getMessage());
    }
}

private boolean sendOverdueNotification(User user) {
    try {
        
        sendEmailNotification(user);
        
        System.out.println("NOTIFICATION: User " + user.getEmail() + 
                          " has overdue loans. Notification sent.");
        
        return true;
        
    } catch (Exception e) {
        System.err.println("Failed to send notification to user " + user.getEmail() + ": " + e.getMessage());
        return false;
    }
}

private void sendEmailNotification(User user) {
    String subject = "Recordatorio: Tienes préstamos vencidos";

    System.out.println("EMAIL to " + user.getEmail() + ": " + subject);
}

    @Override
    public int cleanupInactiveUsers() throws ValidationException {
        try {
            return userDAO.deleteInactiveUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ValidationException("cleanupInactiveUsers", "DATABASE_ERROR",
                    "Error cleaning up inactive users: " + e.getMessage());
        }
    }

   /*  @Override
    public List<User> getUsersRequiringRenewal() throws ValidationException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsersRequiringRenewal'");
    }
    */
    @Override
    public Map<String, Object> getUserStatistics(int userId) throws ValidationException {
        ValidationHelper.validateUserId(userId);

        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new ValidationException("userId", "USER_NOT_FOUND",
                        "User with ID " + userId + " not found");
            }

            Map<String, Object> stats = new HashMap<>();

            stats.put("userInfo", Map.of(
                    "id", user.getUserID(),
                    "name", user.getFirstName() + " " + user.getLastName(),
                    "email", user.getEmail(),
                    "role", user.getRole().name(),
                    "registrationDate", user.getRegistrationDate(),
                    "active", user.isActive()));

            int activeLoans = userDAO.countActiveLoansByUser(userId);
            int loanLimit = user.getRole().getLoanLimit();
            int availableLoans = Math.max(0, loanLimit - activeLoans);

            stats.put("loanStats", Map.of(
                    "activeLoans", activeLoans,
                    "loanLimit", loanLimit,
                    "availableLoans", availableLoans,
                    "loanUtilization", (double) activeLoans / loanLimit * 100));

            stats.put("totalLoansHistory", 0);
            stats.put("overdueLoansCount", 0);
            stats.put("favoriteCategory", "Unknown");

            return stats;

        } catch (SQLException e) {
            throw new ValidationException("userId", "DATABASE_ERROR",
                    "Error retrieving user statistics: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getUsersLoanActivityReport() throws ValidationException {
        try {
            List<Map<String, Object>> report = new ArrayList<>();

            List<User> activeUsers = userDAO.findActiveUsers();

            for (User user : activeUsers) {
                try {
                    int activeLoans = userDAO.countActiveLoansByUser(user.getUserID());
                    int totalLoans = 0;
                    int overdueLoans = 0;

                    Map<String, Object> userReport = new HashMap<>();
                    userReport.put("userId", user.getUserID());
                    userReport.put("userName", user.getFirstName() + " " + user.getLastName());
                    userReport.put("email", user.getEmail());
                    userReport.put("role", user.getRole().name());
                    userReport.put("activeLoans", activeLoans);
                    userReport.put("totalLoans", totalLoans);
                    userReport.put("overdueLoans", overdueLoans);
                    userReport.put("loanLimit", user.getRole().getLoanLimit());
                    userReport.put("utilizationRate", (double) activeLoans / user.getRole().getLoanLimit() * 100);

                    report.add(userReport);

                } catch (SQLException e) {
                    System.err.println("Error processing user " + user.getUserID() + ": " + e.getMessage());
                }
            }

            return report;

        } catch (SQLException e) {
            throw new ValidationException("report", "DATABASE_ERROR",
                    "Error generating loan activity report: " + e.getMessage());
        }
    }

    @Override
public List<Map<String, Object>> getUsersRegistrationReport(int year) throws ValidationException {
    if (year < 1900 || year > LocalDate.now().getYear() + 1) {
        throw new ValidationException("year", "INVALID_YEAR",
                "Year must be between 1900 and " + (LocalDate.now().getYear() + 1));
    }

    try {
        List<Map<String, Object>> report = new ArrayList<>();

        Map<Integer, Integer> monthlyRegistrations = userDAO.getRegistrationsByMonth(year);

        for (int month = 1; month <= 12; month++) {
            Map<String, Object> monthReport = new HashMap<>();
            monthReport.put("year", year);
            monthReport.put("month", month);
            monthReport.put("monthName", getMonthName(month));
            monthReport.put("registrationsCount", monthlyRegistrations.get(month));

            int totalYearRegistrations = monthlyRegistrations.values().stream().mapToInt(Integer::intValue).sum();
            double percentage = totalYearRegistrations > 0
                    ? (double) monthlyRegistrations.get(month) / totalYearRegistrations * 100
                    : 0;
            monthReport.put("percentageOfTotal", percentage);

            report.add(monthReport);
        }

        return report;

    } catch (SQLException e) {
        throw new ValidationException("report", "DATABASE_ERROR",
                "Error generating registration report: " + e.getMessage());
    }
}

private String getMonthName(int month) {
    String[] monthNames = { "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December" };
    return monthNames[month - 1];
}

}
