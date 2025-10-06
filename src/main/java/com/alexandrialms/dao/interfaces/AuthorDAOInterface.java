package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.Author;
import java.sql.SQLException;
import java.util.List;

public interface AuthorDAOInterface extends GenericDAO<Author, Integer> {
    List<Author> findByLastName(String lastName) throws SQLException;
    List<Author> findByNationality(String nationality) throws SQLException;
}
