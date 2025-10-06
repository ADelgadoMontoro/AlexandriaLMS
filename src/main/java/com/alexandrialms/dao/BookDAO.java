package com.alexandrialms.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.alexandrialms.dao.interfaces.BookDAOInterface;
import com.alexandrialms.model.Book;
import com.alexandrialms.util.DBConnection;
import com.alexandrialms.util.ValidationHelper;

public class BookDAO implements BookDAOInterface {
    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                Statement statement = conn.createStatement();
                ResultSet rs = statement
                        .executeQuery("SELECT book_id, title, isbn, publication_year, category_id FROM books;");) {
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Book findById(Integer bookID) {
        if (!ValidationHelper.isValidBookID(bookID,this)){
            return null;
        }
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where book_id = ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookID);
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
    public boolean insert(Book book) {
    String sql = "INSERT INTO books (title, isbn, publication_year, category_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();        
            PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getIsbn());
            pstm.setInt(3, book.getPubYear());
            pstm.setInt(4, book.getCategoryId()); 
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Book book) {
        String sql = "UPDATE Book SET title = ?, isbn = ?, publication_year = ?, category_id = ? WHERE book_id = ?;";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
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

    @Override
    public boolean delete(Integer bookID) {
        if (!ValidationHelper.isValidBookID(bookID, this)){
            return false;
        }
        String sql= "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setInt(1, bookID);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Book mapResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookID(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setIsbn(rs.getString("isbn"));
        book.setPubYear(rs.getInt("publication_year"));
        book.setCategoryId(rs.getInt("category_id"));

        return book;
    }

    @Override
    public Book findByISBN(String isbn) {
        if (!ValidationHelper.isValidISBN(isbn)) {
            return null;
        }
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where isbn = ?;";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, isbn);
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
    public List<Book> findByTitle(String partialTitle) {
        String sql = "SELECT book_id, title, isbn, publication_year, category_id from books where LOWER(title) LIKE LOWER(?);";
        List<Book> books = new ArrayList<>();
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);) {
            pstm.setString(1, "%" + partialTitle + "%");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

}
