package com.alexandrialms.dao;

import com.alexandrialms.model.Author;
import com.alexandrialms.model.Nationality;
import com.alexandrialms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM authors";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Author author = new Author();
                author.setAuthorID(rs.getInt("author_id"));
                author.setFirstName(rs.getString("first_name"));
                author.setLastName(rs.getString("last_name"));
                author.setNationality(rs.getString("nationality"));

                Date birthDate = rs.getDate("birth_date");
                if (birthDate != null) {
                    author.setBirthDate(birthDate.toLocalDate());
                }

                authors.add(author);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    public Author findById(int authorID) {
        String sql = "SELECT author_id, first_name, last_name, nationality, birth_date FROM Authors WHERE author_id = ?";
        Author author = null;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, authorID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    author = new Author();
                    author.setAuthorID(rs.getInt("author_id"));
                    author.setFirstName(rs.getString("first_name"));
                    author.setLastName(rs.getString("last_name"));
                    author.setNationality(rs.getString("nationality"));
                    Date birthDate = rs.getDate("birth_date");
                    if (birthDate != null) {
                        author.setBirthDate(birthDate.toLocalDate());
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return author;
    }

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

    public boolean delete(int authorID) {
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
}