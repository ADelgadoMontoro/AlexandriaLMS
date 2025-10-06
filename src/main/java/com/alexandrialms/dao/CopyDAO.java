package com.alexandrialms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.alexandrialms.dao.interfaces.GenericDAO;
import com.alexandrialms.model.Copy;
import com.alexandrialms.model.CopyStatus;
import com.alexandrialms.util.DBConnection;

public class CopyDAO implements GenericDAO<Copy, Integer>{
        @Override

    public List<Copy> findAll() {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }
    @Override

    public Copy findById(Integer id) {
        String sql = "SELECT * FROM copies WHERE copy_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Copy copy) {
        String sql = "INSERT INTO copies (book_id, internal_code, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, copy.getBook_id());
            stmt.setString(2, copy.getInternal_code());
            stmt.setString(3, copy.getStatus().name());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    copy.setCopyID(generatedKeys.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Copy copy) {
        String sql = "UPDATE copies SET book_id = ?, internal_code = ?, status = ? WHERE copy_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, copy.getBook_id());
            stmt.setString(2, copy.getInternal_code());
            stmt.setString(3, copy.getStatus().name());
            stmt.setInt(4, copy.getCopyID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM copies WHERE copy_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Copy mapResultSet(ResultSet rs) throws SQLException {
        Copy copy = new Copy();
        copy.setCopyID(rs.getInt("copy_id"));
        copy.setBook_id(rs.getInt("book_id"));
        copy.setInternal_code(rs.getString("internal_code"));
        copy.setStatus(CopyStatus.valueOf(rs.getString("status")));
        return copy;
    }
}