package com.alexandrialms.dao.impl;

import com.alexandrialms.dao.interfaces.GenericDAO;
import com.alexandrialms.model.Category;
import com.alexandrialms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements GenericDAO<Category, Integer> {

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
}