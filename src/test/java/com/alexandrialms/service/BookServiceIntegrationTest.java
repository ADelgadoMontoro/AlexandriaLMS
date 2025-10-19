package com.alexandrialms.service;

import com.alexandrialms.service.impl.BookServiceImpl;
import com.alexandrialms.model.Book;
import com.alexandrialms.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceIntegrationTest {

    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(); 
    }

    @Test
    @DisplayName("Should validate search term length")
    void searchBooks_ShortSearchTerm_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> bookService.searchBooks("ab"));
        
        assertTrue(exception.getMessage().contains("3 characters"));
    }

    @Test
    @DisplayName("Should validate publication year range - too old")
    void getBooksByPublicationYear_TooOld_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> bookService.getBooksByPublicationYear(50));
        
        assertTrue(exception.getMessage().contains("500"));
    }

    @Test
    @DisplayName("Should validate publication year range - future")
    void getBooksByPublicationYear_Future_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> bookService.getBooksByPublicationYear(2050));
        
        assertTrue(exception.getMessage().contains("current year"));
    }

    @Test
    @DisplayName("Should validate ISBN format")
    void getBookByISBN_InvalidFormat_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> bookService.getBookByISBN("invalid-isbn"));
        
        assertTrue(exception.getMessage().contains("ISBN"));
    }

    @Test
    @DisplayName("Should return empty for non-existing book ID")
    void getBookById_NonExistingId_ReturnsEmpty() throws ValidationException {
        // Act
        Optional<Book> result = bookService.getBookById(999999);
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return all books")
    void getAllBooks_ReturnsList() {
        // Act
        List<Book> result = bookService.getAllBooks();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return total books count")
    void getTotalBooksCount_ReturnsNumber() {
        // Act
        int result = bookService.getTotalBooksCount();
        
        // Assert
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("Should return available books list")
    void getAvailableBooks_ReturnsList() {
        // Act
        List<Book> result = bookService.getAvailableBooks();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return unavailable books list")
    void getUnavailableBooks_ReturnsList() {
        // Act
        List<Book> result = bookService.getUnavailableBooks();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should validate pagination parameters - negative limit")
    void getAllBooksPaginated_NegativeLimit_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> bookService.getAllBooksPaginated(-1, 0));
        
        assertTrue(exception.getMessage().contains("greater than 0"));
    }

    @Test
    @DisplayName("Should validate pagination parameters - negative offset")
    void getAllBooksPaginated_NegativeOffset_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> bookService.getAllBooksPaginated(10, -1));
        
        assertTrue(exception.getMessage().contains("negative"));
    }

    @Test
    @DisplayName("Should return paginated results with valid parameters")
    void getAllBooksPaginated_ValidParameters_ReturnsList() throws ValidationException {
        // Act
        List<Book> result = bookService.getAllBooksPaginated(10, 0);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() <= 10);
    }

    @Test
    @DisplayName("Should return false for invalid ISBN format in existence check")
    void bookExistsByISBN_InvalidFormat_ReturnsFalse() {
        // Act
        boolean result = bookService.bookExistsByISBN("invalid");
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should validate title in existence check")
    void bookExistsByTitleAndYear_InvalidTitle_ReturnsFalse() {
        // Act
        boolean result = bookService.bookExistsByTitleAndYear("a", 2020);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should validate year in existence check")
    void bookExistsByTitleAndYear_InvalidYear_ReturnsFalse() {
        // Act
        boolean result = bookService.bookExistsByTitleAndYear("Valid Title", 50);
        
        // Assert
        assertFalse(result);
    }
}
