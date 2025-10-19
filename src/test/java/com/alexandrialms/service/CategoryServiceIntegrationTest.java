package com.alexandrialms.service;

import com.alexandrialms.service.impl.CategoryServiceImpl;
import com.alexandrialms.model.Category;
import com.alexandrialms.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceIntegrationTest {

    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl();
    }

    @Test
    @DisplayName("Should validate category name length")
    void createCategory_ShortName_ThrowsException() {
        // Arrange
        Category category = new Category("A", "Valid description");

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> categoryService.createCategory(category));
        
        assertTrue(exception.getMessage().contains("2 characters"));
    }

    @Test
    @DisplayName("Should validate category description")
    void createCategory_NullDescription_ThrowsException() {
        // Arrange
        Category category = new Category("Valid Name", null);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> categoryService.createCategory(category));
        
        assertTrue(exception.getMessage().contains("description"));
    }

    @Test
    @DisplayName("Should validate search name length")
    void getCategoriesByNameContaining_ShortName_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> categoryService.getCategoriesByNameContaining("A"));
        
        assertTrue(exception.getMessage().contains("2 characters"));
    }

    @Test
    @DisplayName("Should validate search description length")
    void getCategoriesByDescriptionContaining_ShortDescription_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> categoryService.getCategoriesByDescriptionContaining("A"));
        
        assertTrue(exception.getMessage().contains("2 characters"));
    }

    @Test
    @DisplayName("Should validate search term length")
    void searchCategories_ShortTerm_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> categoryService.searchCategories("A"));
        
        assertTrue(exception.getMessage().contains("2 characters"));
    }

    @Test
    @DisplayName("Should return empty for non-existing category ID")
    void getCategoryById_NonExistingId_ReturnsEmpty() {
        // Act
        Optional<Category> result = categoryService.getCategoryById(999999);
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return all categories")
    void getAllCategories_ReturnsList() {
        // Act
        List<Category> result = categoryService.getAllCategories();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return empty for non-existing category name")
    void getCategoryByName_NonExistingName_ReturnsEmpty() throws ValidationException {
        // Act
        Optional<Category> result = categoryService.getCategoryByName("NonExistingCategoryNameXYZ123");
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return categories by name containing")
    void getCategoriesByNameContaining_ValidName_ReturnsList() throws ValidationException {
        // Act
        List<Category> result = categoryService.getCategoriesByNameContaining("test");
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return categories by description containing")
    void getCategoriesByDescriptionContaining_ValidDescription_ReturnsList() throws ValidationException {
        // Act
        List<Category> result = categoryService.getCategoriesByDescriptionContaining("test");
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should search categories by term")
    void searchCategories_ValidTerm_ReturnsList() throws ValidationException {
        // Act
        List<Category> result = categoryService.searchCategories("test");
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return zero books for non-existing category")
    void getBooksCountInCategory_NonExistingId_ReturnsZero() throws ValidationException {
        // Act
        int result = categoryService.getBooksCountInCategory(999999);
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should get books count per category")
    void getBooksCountPerCategory_ReturnsMap() {
        // Act
        Map<Integer, Integer> result = categoryService.getBooksCountPerCategory();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return categories with books")
    void getCategoriesWithBooks_ReturnsList() {
        // Act
        List<Category> result = categoryService.getCategoriesWithBooks();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return empty categories")
    void getEmptyCategories_ReturnsList() {
        // Act
        List<Category> result = categoryService.getEmptyCategories();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should validate limit for popular categories")
    void getMostPopularCategories_InvalidLimit_ThrowsException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
            () -> categoryService.getMostPopularCategories(0));
        
        assertTrue(exception.getMessage().contains("greater than 0"));
    }

    @Test
    @DisplayName("Should return most popular categories")
    void getMostPopularCategories_ValidLimit_ReturnsList() throws ValidationException {
        // Act
        List<Category> result = categoryService.getMostPopularCategories(5);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() <= 5);
    }

    @Test
    @DisplayName("Should return total categories count")
    void getTotalCategoriesCount_ReturnsNumber() {
        // Act
        int result = categoryService.getTotalCategoriesCount();
        
        // Assert
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("Should return false for non-existing category ID")
    void categoryExists_NonExistingId_ReturnsFalse() {
        // Act
        boolean result = categoryService.categoryExists(999999);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false for non-existing category name")
    void categoryExistsByName_NonExistingName_ReturnsFalse() {
        // Act
        boolean result = categoryService.categoryExistsByName("NonExistingNameXYZ123");
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should delete empty categories")
    void deleteEmptyCategories_ReturnsCount() throws ValidationException {
        // Act
        int result = categoryService.deleteEmptyCategories();
        
        // Assert
        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("Should return false when updating non-existing category name")
    void updateCategoryName_NonExistingCategory_ReturnsFalse() throws ValidationException {
        // Act
        boolean result = categoryService.updateCategoryName(999999, "New Name");
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should create category with valid data")
    void createCategory_ValidData_CreatesSuccessfully() throws ValidationException {
        // Arrange
        Category category = new Category("Test Category " + System.currentTimeMillis(), "Test Description");
        
        // Act
        Category result = categoryService.createCategory(category);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getCategoryID() > 0);
        assertEquals(category.getName(), result.getName());
    }

    @Test
    @DisplayName("Should find category by existing ID")
    void getCategoryById_ExistingId_ReturnsCategory() throws ValidationException {
        // Arrange - crear categoría
        Category category = new Category("Find Me " + System.currentTimeMillis(), "Test Description");
        Category created = categoryService.createCategory(category);
        
        // Act
        Optional<Category> result = categoryService.getCategoryById(created.getCategoryID());
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("Find Me", result.get().getName().substring(0, 7));
    }
}