package com.alexandrialms.service;

import com.alexandrialms.service.impl.CopyServiceImpl;
import com.alexandrialms.model.Copy;
import com.alexandrialms.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CopyServiceIntegrationTest {

    private CopyServiceImpl copyService;

    @BeforeEach
    void setUp() {
        copyService = new CopyServiceImpl();
    }

    
    @Test
    @DisplayName("Should validate book ID when getting copies by book")
    void getCopiesByBook_InvalidBookId_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> copyService.getCopiesByBook(-1));
        
        assertTrue(exception.getMessage().contains("book ID"));
    }

    @Test
    @DisplayName("Should validate book ID when getting available copies by book")
    void getAvailableCopiesByBook_InvalidBookId_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> copyService.getAvailableCopiesByBook(999999));
        
        assertTrue(exception.getMessage().contains("book ID"));
    }

    @Test
    @DisplayName("Should validate copy ID when checking availability")
    void isCopyAvailable_InvalidCopyId_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> copyService.isCopyAvailable(-1));
        
        assertTrue(exception.getMessage().contains("copy ID"));
    }

    @Test
    @DisplayName("Should validate internal code format")
    void searchCopiesByInventoryNumber_InvalidCode_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> copyService.searchCopiesByInventoryNumber(""));
        
        assertTrue(exception.getMessage().contains("inventory number"));
    }

    @Test
    @DisplayName("Should validate acquisition year range")
    void getCopiesByAcquisitionYear_InvalidYear_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> copyService.getCopiesByAcquisitionYear(100));
        
        assertTrue(exception.getMessage().contains("acquisition year"));
    }

    @Test
    @DisplayName("Should validate acquisition year range")
    void getCopiesByAcquisitionYearRange_InvalidRange_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> copyService.getCopiesByAcquisitionYearRange(2050, 2020));
        
        assertTrue(exception.getMessage().contains("acquisition year range"));
    }

    @Test
    @DisplayName("Should return empty for non-existing copy ID")
    void getCopyById_NonExistingId_ReturnsEmpty() {
        // Act
        Optional<Copy> result = copyService.getCopyById(999999);
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return all copies")
    void getAllCopies_ReturnsList() {
        // Act
        List<Copy> result = copyService.getAllCopies();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return empty list for non-existing book copies")
    void getCopiesByBook_NonExistingBook_ReturnsEmptyList() throws ValidationException {
        // Act
        List<Copy> result = copyService.getCopiesByBook(999999);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return available copies by book")
    void getAvailableCopiesByBook_ValidBook_ReturnsList() throws ValidationException {
        // Act
        List<Copy> result = copyService.getAvailableCopiesByBook(1);
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return unavailable copies by book")
    void getUnavailableCopiesByBook_ValidBook_ReturnsList() throws ValidationException {
        // Act
        List<Copy> result = copyService.getUnavailableCopiesByBook(1);
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return all available copies")
    void getAvailableCopies_ReturnsList() {
        // Act
        List<Copy> result = copyService.getAvailableCopies();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return all unavailable copies")
    void getUnavailableCopies_ReturnsList() {
        // Act
        List<Copy> result = copyService.getUnavailableCopies();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return zero copies count for non-existing book")
    void getCopiesCountByBook_NonExistingBook_ReturnsZero() throws ValidationException {
        // Act
        int result = copyService.getCopiesCountByBook(999999);
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should return zero available copies for non-existing book")
    void getAvailableCopiesCountByBook_NonExistingBook_ReturnsZero() throws ValidationException {
        // Act
        int result = copyService.getAvailableCopiesCountByBook(999999);
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should return zero unavailable copies for non-existing book")
    void getUnavailableCopiesCountByBook_NonExistingBook_ReturnsZero() throws ValidationException {
        // Act
        int result = copyService.getUnavailableCopiesCountByBook(999999);
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should return total copies count")
    void getTotalCopiesCount_ReturnsNumber() {
        // Act
        int result = copyService.getTotalCopiesCount();
        
        // Assert
        assertTrue(result >= 0);
    }

    // TESTS DE GESTIÓN DE PRÉSTAMOS
    @Test
    @DisplayName("Should return copies with active loans")
    void getCopiesWithActiveLoans_ReturnsList() {
        // Act
        List<Copy> result = copyService.getCopiesWithActiveLoans();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return copies by loan status")
    void getCopiesByLoanStatus_ValidStatus_ReturnsList() throws ValidationException {
        // Act 
        List<Copy> result = copyService.getCopiesByLoanStatus("active");
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should search copies by internal code")
    void searchCopiesByInventoryNumber_ValidCode_ReturnsList() throws ValidationException {
        // Act
        List<Copy> result = copyService.searchCopiesByInventoryNumber("test");
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return copies by acquisition year")
    void getCopiesByAcquisitionYear_ValidYear_ReturnsList() throws ValidationException {
        // Act
        List<Copy> result = copyService.getCopiesByAcquisitionYear(2020);
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return copies by acquisition year range")
    void getCopiesByAcquisitionYearRange_ValidRange_ReturnsList() throws ValidationException {
        // Act
        List<Copy> result = copyService.getCopiesByAcquisitionYearRange(2020, 2023);
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return false for non-existing copy ID")
    void copyExists_NonExistingId_ReturnsFalse() {
        // Act
        boolean result = copyService.copyExists(999999);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false for non-existing internal code")
    void copyExistsByInventoryNumber_NonExistingCode_ReturnsFalse() {
        // Act
        boolean result = copyService.copyExistsByInventoryNumber("NONEXISTENT123");
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return orphaned copies")
    void getOrphanedCopies_ReturnsList() {
        // Act
        List<Copy> result = copyService.getOrphanedCopies();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should delete orphaned copies")
    void deleteOrphanedCopies_ReturnsCount() throws ValidationException {
        // Act
        int result = copyService.deleteOrphanedCopies();
        
        // Assert
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("Should return paginated copies by book")
    void getCopiesByBookPaginated_ValidParameters_ReturnsList() throws ValidationException {
        // Act
        List<Copy> result = copyService.getCopiesByBookPaginated(1, 10, 0);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() <= 10);
    }

    @Test
    @DisplayName("Should return paginated available copies")
    void getAvailableCopiesPaginated_ValidParameters_ReturnsList() throws ValidationException {
        // Act
        List<Copy> result = copyService.getAvailableCopiesPaginated(10, 0);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() <= 10);
    }

    @Test
    @DisplayName("Should return false when deleting copies from non-existing book")
    void deleteCopiesByBook_NonExistingBook_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> copyService.deleteCopiesByBook(999999));
        
        assertTrue(exception.getMessage().contains("book ID"));
    }

    @Test
    @DisplayName("Should validate status when updating copies status by book")
    void updateCopiesStatusByBook_InvalidStatus_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> copyService.updateCopiesStatusByBook(1, "INVALID_STATUS"));
        
        assertTrue(exception.getMessage().contains("status"));
    }
}
