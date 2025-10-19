package com.alexandrialms.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.alexandrialms.dao.impl.CategoryDAO;
import com.alexandrialms.exception.ValidationException;
import com.alexandrialms.model.Category;
import com.alexandrialms.service.interfaces.CategoryServiceInterface;
import com.alexandrialms.util.ValidationHelper;

public class CategoryServiceImpl implements CategoryServiceInterface {
    private final CategoryDAO categoryDAO = new CategoryDAO();

@Override
public Category createCategory(Category category) throws ValidationException {
    ValidationHelper.validateCategoryForInsert(category, categoryDAO);
    
    try {
        if (categoryDAO.insert(category)) {
            Category savedCategory = categoryDAO.findByName(category.getName());
            return savedCategory;
        } 
    } catch (Exception e) {
        throw new ValidationException("Failed to create category: " + e.getMessage(), e);
    }
    return null;
}

    @Override
    public Optional<Category> getCategoryById(int categoryId) {
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            return Optional.empty();
        }
        Optional<Category> category = Optional.ofNullable(categoryDAO.findById(categoryId));
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryDAO.findAll();
        return categories;
    }

    @Override
    public Optional<Category> updateCategory(Category category) throws ValidationException {
        ValidationHelper.validateCategoryForUpdate(category, categoryDAO);
        
        try {
            if (categoryDAO.update(category)) {
                Category updatedCategory = categoryDAO.findById(category.getCategoryID());
                return Optional.ofNullable(updatedCategory);
            } 
        } catch (Exception e) {
            throw new ValidationException("Failed to update category: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteCategory(int categoryId) throws ValidationException {
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            throw new ValidationException("categoryId", "INVALID_ID", "Invalid category ID: " + categoryId);
        }
        try {
            return categoryDAO.delete(categoryId);
        } catch (Exception e) {
            throw new ValidationException("Failed to delete category: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Category> getCategoryByName(String name) throws ValidationException {
        if (!ValidationHelper.isValidString(name, 2)) {
            throw new ValidationException("name", "INVALID_NAME", "Invalid category name: " + name);
        }
        Optional<Category> category = Optional.ofNullable(categoryDAO.findByName(name));
        return category;
    }

    @Override
    public List<Category> getCategoriesByNameContaining(String name) throws ValidationException {
        if (!ValidationHelper.isValidString(name, 2)) {
            throw new ValidationException("name", "INVALID_NAME", "Invalid category name: " + name + ". It should have at least 2 characters.");
            
        }
        List<Category> categories = categoryDAO.findByNameContaining(name);
        return categories;
    }

    @Override
    public List<Category> getCategoriesByDescriptionContaining(String description) throws ValidationException {
        if (!ValidationHelper.isValidString(description, 2)) {
            throw new ValidationException("description", "INVALID_DESCRIPTION", "Invalid category description: " + description + ". It should have at least 2 characters.");
        }
        List<Category> categories = categoryDAO.findByDescriptionContaining(description);
        return categories;
    }

    @Override
    public List<Category> searchCategories(String searchTerm) throws ValidationException {
        if (!ValidationHelper.isValidString(searchTerm, 2)) {
            throw new ValidationException("searchTerm", "INVALID_SEARCH_TERM", "Invalid search term: " + searchTerm + ". It should have at least 2 characters.");
        }
        List<Category> categories = categoryDAO.searchCategories(searchTerm);
        return categories;
    }

    @Override
    public int getBooksCountInCategory(int categoryId) throws ValidationException {
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            return 0;
        }
        int count = categoryDAO.countBooksInCategory(categoryId);
        return count;
    }

    @Override
    public Map<Integer, Integer> getBooksCountPerCategory() {
        Map<Integer, Integer> counts = categoryDAO.getBooksCountPerCategory();
        return counts;
    }

    @Override
    public List<Category> getCategoriesWithBooks() {
        List<Category> categories = categoryDAO.findCategoriesWithBooks();
        return categories;
    }

    @Override
    public List<Category> getEmptyCategories() {
        List<Category> categories = categoryDAO.findEmptyCategories();
        return categories;
    }

    @Override
    public List<Category> getMostPopularCategories(int limit) throws ValidationException {
        ValidationHelper.validateLimit(limit);
        List<Category> categories = categoryDAO.findMostPopularCategories(limit);
        return categories;
    }

    @Override
    public int getTotalCategoriesCount() {
        int count = categoryDAO.countAllCategories();
        return count;
    }

    @Override
    public boolean categoryExists(int categoryId) {
        boolean exists = ValidationHelper.isValidCategoryID(categoryId, categoryDAO);
        return exists;
    }

    @Override
    public boolean categoryExistsByName(String name) {
        return categoryDAO.existsByName(name);
    }

    @Override
    public int deleteEmptyCategories() throws ValidationException {
        try {
            int deletedCount = categoryDAO.deleteEmptyCategories();
            return deletedCount;
        } catch (Exception e) {
            throw new ValidationException("Failed to delete empty categories: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateCategoryName(int categoryId, String newName) throws ValidationException {
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            return false;
        }
        if (!ValidationHelper.isValidString(newName, 2)) {
            return false;
        }
        try {
            return categoryDAO.updateCategoryName(categoryId, newName);
        } catch (Exception e) {
            throw new ValidationException("Failed to update category name: " + e.getMessage(), e);
        }
    }
    
}
