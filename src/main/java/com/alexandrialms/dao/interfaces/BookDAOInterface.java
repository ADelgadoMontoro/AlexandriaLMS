package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.Book;
import java.sql.SQLException;
import java.util.List;

public interface BookDAOInterface extends GenericDAO<Book, Integer> {
    Book findByISBN(String isbn) throws SQLException;
    List<Book> findByTitle(String partialTitle) throws SQLException;
}
