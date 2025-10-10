package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.Author;
import java.sql.SQLException;
import java.util.List;

public interface AuthorDAOInterface extends GenericDAO<Author, Integer> {
    
    List<Author> findByLastName(String lastName) throws SQLException;
    List<Author> findByNationality(String nationality) throws SQLException;
    
    // NEW SEARCH METHODS
    List<Author> findByFirstName(String firstName) throws SQLException;
    List<Author> findByFullName(String firstName, String lastName) throws SQLException;
    List<Author> findByNameContaining(String name) throws SQLException; // Busca en firstName y lastName
    List<Author> findByBirthYear(int year) throws SQLException;
    List<Author> findByBirthDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) throws SQLException;
    
    // SEARCH & FILTER METHODS
    List<Author> searchAuthors(String searchTerm) throws SQLException; // Búsqueda en nombre + apellido + nacionalidad
    
    // STATISTICS & AGGREGATION METHODS - NECESITA DE LA VISTA author_books_summary
    List<Author> findAuthorsWithBooks() throws SQLException; // NECESITA DE LA VISTA author_books_summary
    List<Author> findAuthorsWithMoreThanXBooks(int minBooks) throws SQLException; // NECESITA DE LA VISTA author_books_summary
    List<Author> findMostProlificAuthors(int limit) throws SQLException; // NECESITA DE LA VISTA author_books_summary
    
    // BATCH OPERATIONS
    int deleteAuthorsWithNoBooks() throws SQLException;
    int updateNationality(String oldNationality, String newNationality) throws SQLException;
    
    // VALIDATION & UTILITY METHODS
    boolean existsByFullName(String firstName, String lastName) throws SQLException;
    int countByNationality(String nationality) throws SQLException;
    List<String> findAllNationalities() throws SQLException;
    
    // BIRTHDATE RELATED METHODS
    List<Author> findAuthorsBornBefore(java.time.LocalDate date) throws SQLException;
    List<Author> findAuthorsBornAfter(java.time.LocalDate date) throws SQLException;
    List<Author> findLivingAuthors() throws SQLException; // Nacidos después de 1900, por ejemplo
}