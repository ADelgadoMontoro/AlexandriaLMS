package com.alexandrialms.service;

import com.alexandrialms.service.impl.AuthorServiceImpl;
import com.alexandrialms.model.Author;
import com.alexandrialms.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorServiceIntegrationTest {

    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl(); 
    }

    @Test
    @DisplayName("Should validate author name length")
    void createAuthor_ShortFirstName_ThrowsException() {
        // Arrange
        Author author = new Author("A", "ValidLastName", "Spanish", LocalDate.of(1980, 1, 1));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> authorService.createAuthor(author));
        
        assertTrue(exception.getMessage().contains("2 characters"));
    }

    @Test
    @DisplayName("Should validate author last name length")
    void createAuthor_ShortLastName_ThrowsException() {
        // Arrange
        Author author = new Author("ValidFirstName", "B", "Spanish", LocalDate.of(1980, 1, 1));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> authorService.createAuthor(author));
        
        assertTrue(exception.getMessage().contains("2 characters"));
    }

    @Test
    @DisplayName("Should validate future birth date")
    void createAuthor_FutureBirthDate_ThrowsException() {
        // Arrange
        Author author = new Author("John", "Doe", "American", LocalDate.now().plusYears(1));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> authorService.createAuthor(author));
        
        assertTrue(exception.getMessage().contains("future"));
    }

    @Test
    @DisplayName("Should validate author too young")
    void createAuthor_TooYoung_ThrowsException() {
        // Arrange
        Author author = new Author("Baby", "Author", "American", LocalDate.now().minusYears(5));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> authorService.createAuthor(author));
        
        assertTrue(exception.getMessage().contains("10 years"));
    }

    // TESTS DE BÚSQUEDA
    @Test
    @DisplayName("Should return empty for non-existing author ID")
    void findAuthorById_NonExistingId_ReturnsEmpty() {
        // Act
        Optional<Author> result = authorService.findAuthorById(999999);
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return all authors")
    void findAllAuthors_ReturnsList() {
        // Act
        List<Author> result = authorService.findAllAuthors();
        
        // Assert
        assertNotNull(result);
        // No assertion on size because it depends on DB state
    }

    @Test
    @DisplayName("Should validate search name length")
    void findAuthorsByName_ShortName_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> authorService.findAuthorsByName("A"));
        
        assertTrue(exception.getMessage().contains("2 characters"));
    }

    @Test
    @DisplayName("Should return authors by name search")
    void findAuthorsByName_ValidName_ReturnsList() throws ValidationException {
        // Act
        List<Author> result = authorService.findAuthorsByName("test");
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should validate nationality search length")
    void findAuthorsByNationality_ShortNationality_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> authorService.findAuthorsByNationality("U"));
        
        assertTrue(exception.getMessage().contains("2 characters"));
    }

    @Test
    @DisplayName("Should return authors by nationality")
    void findAuthorsByNationality_ValidNationality_ReturnsList() throws ValidationException {
        // Act
        List<Author> result = authorService.findAuthorsByNationality("spanish");
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return false for non-existing author ID in existence check")
    void authorExists_NonExistingId_ReturnsFalse() {
        // Act
        boolean result = authorService.authorExists(999999);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return total authors count")
    void countAuthors_ReturnsNumber() {
        // Act
        int result = authorService.countAuthors();
        
        // Assert
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("Should return empty when updating non-existing author")
    void updateAuthor_NonExistingAuthor_ReturnsEmpty() {
        // Arrange
        Author author = new Author("Non", "Existing", "Unknown", LocalDate.of(1980, 1, 1));
        author.setAuthorID(999999);

        // Act
        Optional<Author> result = authorService.updateAuthor(author);
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return false when deleting non-existing author")
    void deleteAuthor_NonExistingId_ReturnsFalse() throws ValidationException {
        // Act
        boolean result = authorService.deleteAuthor(999999);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return authors with books list")
    void findAuthorsWithBooks_ReturnsList() {
        // Act
        List<Author> result = authorService.findAuthorsWithBooks();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should delete authors with no books")
    void deleteAuthorsWithNoBooks_ReturnsCount() {
        // Act
        int result = authorService.deleteAuthorsWithNoBooks();
        
        // Assert
        assertTrue(result >= 0);
    }

    // TESTS DE CREACIÓN EXITOSA (si tienes datos de prueba)
    @Test
    @DisplayName("Should create author with valid data")
    void createAuthor_ValidData_CreatesSuccessfully() throws ValidationException {
        // Arrange
        Author author = new Author("Test", "Author", "TestNationality", LocalDate.of(1980, 1, 1));
        
        // Act
        Author result = authorService.createAuthor(author);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getAuthorID() > 0);
        assertEquals("Test", result.getFirstName());
        assertEquals("Author", result.getLastName());
    }

    @Test
    @DisplayName("Should find author by existing ID")
    void findAuthorById_ExistingId_ReturnsAuthor() throws ValidationException {
        // Arrange - primero crear un autor
        Author author = new Author("Find", "Me", "Test", LocalDate.of(1970, 1, 1));
        Author created = authorService.createAuthor(author);
        
        // Act
        Optional<Author> result = authorService.findAuthorById(created.getAuthorID());
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("Find", result.get().getFirstName());
        assertEquals("Me", result.get().getLastName());
    }
}