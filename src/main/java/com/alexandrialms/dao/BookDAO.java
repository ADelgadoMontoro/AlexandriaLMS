package com.alexandrialms.dao;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alexandrialms.model.Book;
import com.alexandrialms.util.DBConnection;

public class BookDAO {

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                Statement statement = conn.createStatement();
                ResultSet rs = statement
                        .executeQuery("SELECT book_id, title, isbn, publication_year, category_id FROM books;");) {
            while (rs.next()) {
                Book book = new Book();
                book.setBookID(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setIsbn(rs.getString("isbn"));
                book.setPubYear(rs.getInt("publication_year"));
                book.setCategoryId(rs.getInt("category_id"));
                books.add(book);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Book findById(int bookID) {
        Book book = null;
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(
                        "SELECT book_id, title, isbn, publication_year, category_id from books where book_id = ?;");) {
            pstm.setInt(1, bookID);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                book = new Book();
                book.setBookID(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setIsbn(rs.getString("isbn"));
                book.setPubYear(rs.getInt("publication_year"));
                book.setCategoryId(rs.getInt("category_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public boolean insert(Book book) {

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO books (title, isbn, publication_year, category_id) VALUES (?,?,?,?)");) {
            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getIsbn());
            pstm.setInt(3, book.getPubYear());
            pstm.setInt(4, book.getCategoryId()); // TO DO: crear un método que valide que el category_id está entre 1 y
                                                  // la categoría máxima
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Book book) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(
                        "UPDATE Book SET title = ?, isbn = ?, publication_year = ?, category_id = ? WHERE book_id = ?");) {
            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getIsbn());
            pstm.setInt(3, book.getPubYear()); // TO DO: crear un método que valide que el category_id está entre 1 y la
                                               // categoría máxima
            pstm.setInt(4, book.getCategoryId());
            pstm.setInt(5, book.getBookID());

            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int bookID) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement("DELETE FROM books WHERE book_id = ?");) {
            pstm.setInt(1, bookID);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
