package com.alexandrialms.service.interfaces;

import com.alexandrialms.model.Category;
import com.alexandrialms.exception.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryServiceInterface {

    // CRUD OPERATIONS
    Category createCategory(Category category) throws ValidationException;
    Optional<Category> getCategoryById(int categoryId);
    List<Category> getAllCategories();
    Optional<Category> updateCategory(Category category) throws ValidationException;
    boolean deleteCategory(int categoryId) throws ValidationException;

    // SEARCH OPERATIONS
    Optional<Category> getCategoryByName(String name) throws ValidationException;
    List<Category> getCategoriesByNameContaining(String name) throws ValidationException;
    List<Category> getCategoriesByDescriptionContaining(String description) throws ValidationException;
    List<Category> searchCategories(String searchTerm) throws ValidationException;

    // STATISTICS OPERATIONS
    int getBooksCountInCategory(int categoryId) throws ValidationException;
    Map<Integer, Integer> getBooksCountPerCategory();
    List<Category> getCategoriesWithBooks();
    List<Category> getEmptyCategories();
    List<Category> getMostPopularCategories(int limit) throws ValidationException;
    int getTotalCategoriesCount();

    // VALIDATION OPERATIONS
    boolean categoryExists(int categoryId);
    boolean categoryExistsByName(String name);
    
    // MAINTENANCE OPERATIONS
    int deleteEmptyCategories() throws ValidationException;
    boolean updateCategoryName(int categoryId, String newName) throws ValidationException;
}
