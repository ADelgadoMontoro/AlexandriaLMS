package com.alexandrialms.dao.impl;

import com.alexandrialms.dao.interfaces.CategoryDAOInterface;
import com.alexandrialms.model.Category;
import com.alexandrialms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryDAO implements CategoryDAOInterface {

    @Override
    public boolean insert(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, category.getName());
            pstm.setString(2, category.getDescription());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, category.getName());
            pstm.setString(2, category.getDescription());
            pstm.setInt(3, category.getCategoryID());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer categoryID) {
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, categoryID);

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Category findById(Integer categoryID) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, categoryID);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    private Category mapResultSet(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryID(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        return category;
    }

    @Override
    public Category findByName(String name) {
        String sql = "SELECT * FROM categories WHERE name = ?;";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, name);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();}
        return null;
    }

    @Override
    public List<Category> findByNameContaining(String name) {
        String sql = "SELECT * FROM categories WHERE name LIKE ?;";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, "%" + name + "%");

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                categories.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();}
        return categories;
    }

    @Override
    public List<Category> findByDescriptionContaining(String description) {
        String sql = "SELECT * FROM categories WHERE description LIKE ?;";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, "%" + description + "%");

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                categories.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();}
        return categories;
    }

    @Override
    public int countBooksInCategory(int categoryId) {
        String sql = "SELECT COUNT(*) AS book_count FROM books WHERE category_id = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, categoryId);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("book_count");
            }

        } catch (SQLException e) {
            e.printStackTrace();}
        return 0;
    }

    @Override
    public Map<Integer, Integer> getBooksCountPerCategory() {
        Map <Integer, Integer> booksCountMap = new java.util.HashMap<>();
        String sql = "SELECT category_id, COUNT(*) AS book_count FROM books GROUP BY category_id;";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                booksCountMap.put(rs.getInt("category_id"), rs.getInt("book_count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();}
        return booksCountMap;

    }

    @Override
    public List<Category> findCategoriesWithBooks() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT c.* FROM categories c " +
                     "JOIN books b ON c.category_id = b.category_id;";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();}
        return categories;
    }

    @Override
    public List<Category> findEmptyCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT c.* FROM categories c " +
                     "LEFT JOIN books b ON c.category_id = b.category_id " +
                     "WHERE b.book_id IS NULL;";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();}
        return categories;
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM categories WHERE name = ? LIMIT 1;";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, name);

            ResultSet rs = pstm.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();}
        return false;
    }

    @Override
    public int countAllCategories() {
        String sql = "SELECT COUNT(*) AS total FROM categories;";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();}
        return 0;
    }

    @Override
    public int deleteEmptyCategories() {
        String sql = "DELETE FROM categories WHERE category_id NOT IN (SELECT DISTINCT category_id FROM books);";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            return stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();}
        return 0;
    }

    @Override
    public boolean updateCategoryName(int categoryId, String newName) {
        String sql = "UPDATE categories SET name = ? WHERE category_id = ?;";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, newName);
            pstm.setInt(2, categoryId);

            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();}
        return false;
    }

    @Override
    public List<Category> searchCategories(String searchTerm) {
        String sql = "SELECT * FROM categories WHERE name LIKE ? OR description LIKE ?;";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            String likeTerm = "%" + searchTerm + "%";
            pstm.setString(1, likeTerm);
            pstm.setString(2, likeTerm);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                categories.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();}
        return categories;
    }

    @Override
    public List<Category> findMostPopularCategories(int limit) {
        String sql = "SELECT c.*, COUNT(b.book_id) AS book_count " +
                     "FROM categories c " +
                     "JOIN books b ON c.category_id = b.category_id " +
                     "GROUP BY c.category_id " +
                     "ORDER BY book_count DESC " +
                     "LIMIT ?;";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, limit);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                categories.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();}
        return categories;
    }
}