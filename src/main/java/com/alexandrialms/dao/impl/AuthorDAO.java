package com.alexandrialms.dao.impl;

import com.alexandrialms.dao.interfaces.AuthorDAOInterface;
import com.alexandrialms.model.Author;
import com.alexandrialms.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthorDAO implements AuthorDAOInterface {
    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM authors";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                authors.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return authors;
    }

    @Override
    public Author findById(Integer authorID) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM authors WHERE author_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, authorID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Author author) {
        String sql = "INSERT INTO Authors (first_name, last_name, nationality, birth_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, author.getFirstName());
            pstmt.setString(2, author.getLastName());
            pstmt.setString(3, author.getNationality());
            if (author.getBirthDate() != null) {
                pstmt.setDate(4, Date.valueOf(author.getBirthDate()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setAuthorID(generatedKeys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean update(Author author) {
        String sql = "UPDATE authors SET first_name = ?, last_name = ?, nationality = ?, birth_date = ? WHERE author_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, author.getFirstName());
            pstm.setString(2, author.getLastName());
            pstm.setString(3, author.getNationality());
            pstm.setDate(4, java.sql.Date.valueOf(author.getBirthDate()));
            pstm.setInt(5, author.getAuthorID());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer authorID) {
        String sql = "DELETE FROM authors WHERE author_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, authorID);

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    Author mapResultSet(ResultSet rs) throws SQLException { // Visible a nivel de package para que BookDAO pueda usarlo
        Author author = new Author();
        author.setAuthorID(rs.getInt("author_id"));
        author.setFirstName(rs.getString("first_name"));
        author.setLastName(rs.getString("last_name"));
        author.setNationality(rs.getString("nationality"));

        Date birthDate = rs.getDate("birth_date");
        if (birthDate != null) {
            author.setBirthDate(birthDate.toLocalDate());
        }
        return author;

    }

    @Override
    public List<Author> findByLastName(String lastName) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM authors WHERE LOWER (last_name) LIKE LOWER (?)";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + lastName + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return authors;
    }

    @Override
    public List<Author> findByNationality(String nationality) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM Authors WHERE LOWER (nationality) LIKE LOWER (?)";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nationality + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return authors;
    }

    @Override
    public List<Author> findByFirstName(String firstName) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM authors WHERE LOWER (first_name) LIKE LOWER (?)";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + firstName + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return authors;
    }

    @Override
    public List<Author> findByFullName(String firstName, String lastName) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM authors WHERE LOWER(first_name) = LOWER(?) AND LOWER(last_name) = LOWER(?);";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName.trim());
            pstmt.setString(2, lastName.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return authors;
    }

    @Override
    public List<Author> findByNameContaining(String name) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM authors WHERE LOWER(first_name) LIKE LOWER(?) OR LOWER(last_name) LIKE LOWER(?);";
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name.trim() + "%");
            pstmt.setString(2, "%" + name.trim() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return authors;
    }

    @Override
    public List<Author> findByBirthYear(int year) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM authors where YEAR(birth_date ) = ?;";
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, year);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return authors;
    }

    @Override
    public List<Author> findByBirthDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM authors where birth_date BETWEEN ? AND ?;";
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return authors;
    }

    @Override
    public List<Author> searchAuthors(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date " +
                "FROM authors WHERE LOWER(first_name) LIKE LOWER(?) " +
                "OR LOWER(last_name) LIKE LOWER(?) " +
                "OR LOWER(nationality) LIKE LOWER(?)";

        List<Author> authors = new ArrayList<>();
        String searchPattern = "%" + searchTerm.trim() + "%";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public List<Author> findAuthorsWithBooks() {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM author_books_summary WHERE total_books > 0;";
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return authors;
    }

    @Override
    public List<Author> findAuthorsWithMoreThanXBooks(int minBooks) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM author_books_summary WHERE total_books >= ?;";
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, minBooks);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return authors;
    }

    @Override
    public List<Author> findMostProlificAuthors(int limit) {
        if (limit <= 0) {
            return Collections.emptyList();
        }

        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date " +
                "FROM author_books_summary WHERE total_books > 0 " +
                "ORDER BY total_books DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Author> authors = new ArrayList<>();
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
                return authors;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public int deleteAuthorsWithNoBooks() {
        String sql = "DELETE FROM authors WHERE author_id IN (SELECT author_id FROM author_books_summary WHERE total_books = 0)";
        int affectedRows = 0;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int updateNationality(String oldNationality, String newNationality) {
        if (oldNationality == null || newNationality == null) {
            throw new IllegalArgumentException("Nationalities cannot be null");
        }

        if (oldNationality.trim().isEmpty() || newNationality.trim().isEmpty()) {
            throw new IllegalArgumentException("Nationalities cannot be empty");
        }

        if (oldNationality.equalsIgnoreCase(newNationality)) {
            return 0;
        }

        String sql = "UPDATE authors SET nationality = ? WHERE LOWER(nationality) = LOWER(?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newNationality.trim());
            pstmt.setString(2, oldNationality.trim());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean existsByFullName(String firstName, String lastName) {
        if (firstName == null || lastName == null ||
                firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT 1 FROM authors WHERE LOWER(first_name) = LOWER(?) AND LOWER(last_name) = LOWER(?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName.trim());
            pstmt.setString(2, lastName.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int countByNationality(String nationality) {
        try {
            if (nationality == null || nationality.trim().isEmpty()) {
                return 0;
            }

            String sql = "SELECT COUNT(*) as author_count FROM authors WHERE LOWER(nationality) = LOWER(?)";

            try (Connection conn = DBConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, nationality.trim());

                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next() ? rs.getInt("author_count") : 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<String> findAllNationalities() {
        String sql = "SELECT DISTINCT nationality FROM authors WHERE nationality IS NOT NULL ORDER BY nationality";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            List<String> nationalities = new ArrayList<>();
            while (rs.next()) {
                nationalities.add(rs.getString("nationality"));
            }
            return nationalities;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Author> findAuthorsBornBefore(LocalDate date) {
        if (date == null) {
            return Collections.emptyList();
        }

        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date " +
                "FROM authors WHERE birth_date < ? ORDER BY birth_date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Author> authors = new ArrayList<>();
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
                return authors;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Author> findAuthorsBornAfter(LocalDate date) {
        if (date == null) {
            return Collections.emptyList();
        }

        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date " +
                "FROM authors WHERE birth_date > ? ORDER BY birth_date ASC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Author> authors = new ArrayList<>();
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
                return authors;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Author> findLivingAuthors() {
        LocalDate cutoffDate = LocalDate.now().minusYears(100);

        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date " +
                "FROM authors WHERE birth_date IS NOT NULL AND birth_date > ? " +
                "ORDER BY birth_date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(cutoffDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Author> authors = new ArrayList<>();
                while (rs.next()) {
                    authors.add(mapResultSet(rs));
                }
                return authors;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}