package com.alexandrialms.service;

import com.alexandrialms.service.impl.UserServiceImpl;
import com.alexandrialms.model.User;
import com.alexandrialms.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceIntegrationTest {

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(); 
    }

    @Test
    @DisplayName("Should validate email format")
    void getUserByEmail_InvalidFormat_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.getUserByEmail("invalid-email"));
        
        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    @DisplayName("Should validate user ID - zero")
    void getUserById_ZeroId_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.getUserById(0));
        
        assertTrue(exception.getMessage().contains("greater than 0"));
    }

    @Test
    @DisplayName("Should validate user ID - negative")
    void getUserById_NegativeId_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.getUserById(-1));
        
        assertTrue(exception.getMessage().contains("greater than 0"));
    }

    @Test
    @DisplayName("Should return empty for non-existing user ID")
    void getUserById_NonExistingId_ReturnsEmpty() throws ValidationException {
        // Act
        Optional<User> result = userService.getUserById(999999);
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should validate search term length")
    void searchUsers_ShortSearchTerm_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.searchUsers("ab"));
        
        assertTrue(exception.getMessage().contains("3 characters"));
    }

    @Test
    @DisplayName("Should validate role parameter - empty")
    void getUsersByRole_EmptyRole_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.getUsersByRole(""));
        
        assertTrue(exception.getMessage().contains("Role"));
    }

    @Test
    @DisplayName("Should validate role parameter - null")
    void getUsersByRole_NullRole_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.getUsersByRole(null));
        
        assertTrue(exception.getMessage().contains("Role"));
    }

    @Test
    @DisplayName("Should return all users")
    void getAllUsers_ReturnsList() {
        // Act
        List<User> result = userService.getAllUsers();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return active users")
    void getActiveUsers_ReturnsList() {
        // Act
        List<User> result = userService.getActiveUsers();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return total users count")
    void getTotalUsersCount_ReturnsNumber() {
        // Act
        int result = userService.getTotalUsersCount();
        
        // Assert
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("Should return active users count")
    void getActiveUsersCount_ReturnsNumber() {
        // Act
        int result = userService.getActiveUsersCount();
        
        // Assert
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("Should validate pagination parameters - negative limit")
    void getAllUsersPaginated_NegativeLimit_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.getAllUsersPaginated(-1, 0));
        
        assertTrue(exception.getMessage().contains("greater than 0"));
    }

    @Test
    @DisplayName("Should validate pagination parameters - negative offset")
    void getAllUsersPaginated_NegativeOffset_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.getAllUsersPaginated(10, -1));
        
        assertTrue(exception.getMessage().contains("negative"));
    }

    @Test
    @DisplayName("Should return paginated results with valid parameters")
    void getAllUsersPaginated_ValidParameters_ReturnsList() throws ValidationException {
        // Act
        List<User> result = userService.getAllUsersPaginated(10, 0);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() <= 10);
    }

    @Test
    @DisplayName("Should return false for invalid email format in existence check")
    void userExistsByEmail_InvalidFormat_ReturnsFalse() {
        // Act
        boolean result = userService.userExistsByEmail("invalid-email");
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false for invalid phone format in existence check")
    void userExistsByPhone_InvalidFormat_ReturnsFalse() {
        // Act
        boolean result = userService.userExistsByPhone("invalid-phone");
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should validate user ID in loan eligibility check")
    void isUserEligibleForLoan_InvalidUserId_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.isUserEligibleForLoan(0));
        
        assertTrue(exception.getMessage().contains("greater than 0"));
    }

    @Test
    @DisplayName("Should validate user ID in active status check")
    void isUserActive_InvalidUserId_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.isUserActive(0));
        
        assertTrue(exception.getMessage().contains("greater than 0"));
    }

    @Test
    @DisplayName("Should return users with active loans")
    void getUsersWithActiveLoans_ReturnsList() throws ValidationException {
        // Act
        List<User> result = userService.getUsersWithActiveLoans();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return users with overdue loans")
    void getUsersWithOverdueLoans_ReturnsList() throws ValidationException {
        // Act
        List<User> result = userService.getUsersWithOverdueLoans();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should validate date range - start after end")
    void getUsersByRegistrationDateRange_InvalidRange_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.getUsersByRegistrationDateRange(
                java.time.LocalDate.of(2024, 1, 1),
                java.time.LocalDate.of(2023, 1, 1)
            ));
        
        assertTrue(exception.getMessage().contains("after start date"));
    }

    @Test
    @DisplayName("Should validate date range - future start date")
    void getUsersByRegistrationDateRange_FutureStartDate_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> userService.getUsersByRegistrationDateRange(
                java.time.LocalDate.now().plusDays(1),
                java.time.LocalDate.now().plusDays(10)
            ));
        
        assertTrue(exception.getMessage().contains("future"));
    }
}