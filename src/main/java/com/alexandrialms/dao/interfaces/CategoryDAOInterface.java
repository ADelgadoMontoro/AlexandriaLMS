package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.Category;
import java.util.List;
import java.util.Map;

public interface CategoryDAOInterface extends GenericDAO<Category, Integer> {
    
    // SPECIFIC FINDERS
    Category findByName(String name);
    List<Category> findByNameContaining(String name);
    List<Category> findByDescriptionContaining(String description);
    
    // STATISTICAL QUERIES
    int countBooksInCategory(int categoryId);
    Map<Integer, Integer> getBooksCountPerCategory();
    List<Category> findCategoriesWithBooks();
    List<Category> findEmptyCategories();
    
    // VALIDATION METHODS
    boolean existsByName(String name);
    int countAllCategories();
    
    // MAINTENANCE METHODS
    int deleteEmptyCategories();
    boolean updateCategoryName(int categoryId, String newName);
    
    // ADVANCED SEARCH METHODS
    List<Category> searchCategories(String searchTerm);
    List<Category> findMostPopularCategories(int limit);
}