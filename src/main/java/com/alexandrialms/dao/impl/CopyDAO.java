package com.alexandrialms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.alexandrialms.dao.interfaces.CopyDAOInterface;
import com.alexandrialms.dao.interfaces.GenericDAO;
import com.alexandrialms.model.Copy;
import com.alexandrialms.model.CopyStatus;
import com.alexandrialms.util.DBConnection;

public class CopyDAO implements CopyDAOInterface, GenericDAO<Copy, Integer> {
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

    @Override
    public List<Copy> findByBookId(int bookId) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }

    @Override
    public List<Copy> findAvailableByBookId(int bookId) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE book_id = ? AND status = 'AVAILABLE'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }

    @Override
    public List<Copy> findUnavailableByBookId(int bookId) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE book_id = ? AND status = 'UNAVAILABLE'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }

    @Override
    public List<Copy> findAvailableCopies() {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE status = 'AVAILABLE'";
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
    public List<Copy> findUnavailableCopies() {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE status = 'UNAVAILABLE'";
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
    public boolean isCopyAvailable(int copyId) {
        String sql = "SELECT status FROM copies WHERE copy_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, copyId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                return CopyStatus.valueOf(status) == CopyStatus.AVAILABLE;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setCopyStatus(int copyId, String status) {
        String sql = "UPDATE copies SET status = ? WHERE copy_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, copyId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int countCopiesByBook(int bookId) {
        String sql = "SELECT COUNT(*) AS total FROM copies WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int countAvailableCopiesByBook(int bookId) {
        String sql = "SELECT COUNT(*) AS total FROM copies WHERE book_id = ? AND status = 'AVAILABLE'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int countUnavailableCopiesByBook(int bookId) {
        String sql = "SELECT COUNT(*) AS total FROM copies WHERE book_id = ? AND status = 'UNAVAILABLE'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Copy> findCopiesWithActiveLoans() {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT c.* FROM copies c JOIN loans l ON c.copy_id = l.copy_id WHERE l.return_date IS NULL";
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
    public List<Copy> findCopiesByLoanStatus(String loanStatus) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT c.* FROM copies c JOIN loans l ON c.copy_id = l.copy_id WHERE l.status = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loanStatus);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }

    @Override
    public List<Copy> searchCopiesByInternalCode(String inventoryNumber) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE internal_code LIKE ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + inventoryNumber + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }

    @Override
    public List<Copy> findCopiesByAcquisitionYear(int year) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE YEAR(acquisition_date) = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }

    @Override
    public List<Copy> findCopiesByAcquisitionYearRange(int startYear, int endYear) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE YEAR(acquisition_date) BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, startYear);
            stmt.setInt(2, endYear);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }

    @Override
    public boolean existsByInternalCode(String inventoryNumber) {
        String sql = "SELECT 1 FROM copies WHERE internal_code = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inventoryNumber);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int countAllCopies() {
        String sql = "SELECT COUNT(*) AS total FROM copies";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteCopiesByBook(int bookId) {
        String sql = "DELETE FROM copies WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateCopiesStatusByBook(int bookId, String newStatus) {
        String sql = "UPDATE copies SET status = ? WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, bookId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Copy> findOrphanedCopies() {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT c.* FROM copies c LEFT JOIN books b ON c.book_id = b.book_id WHERE b.book_id IS NULL";
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
    public List<Copy> findByBookIdPaginated(int bookId, int limit, int offset) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE book_id = ? LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }

    @Override
    public List<Copy> findAvailableCopiesPaginated(int limit, int offset) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE status = 'AVAILABLE' LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return copies;
    }

    @Override
    public Copy findByInternalCode(String internalCode) {
        String sql = "SELECT * FROM copies WHERE internal_code = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, internalCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}