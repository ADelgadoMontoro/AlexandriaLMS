package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.Author;
import java.util.List;

public interface AuthorDAOInterface extends GenericDAO<Author, Integer> {
    
    List<Author> findByLastName(String lastName);
    List<Author> findByNationality(String nationality);
    
    // NEW SEARCH METHODS
    List<Author> findByFirstName(String firstName);
    List<Author> findByFullName(String firstName, String lastName) ;
    List<Author> findByNameContaining(String name); // Busca en firstName y lastName
    List<Author> findByBirthYear(int year);
    List<Author> findByBirthDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate);
    
    // SEARCH & FILTER METHODS
    List<Author> searchAuthors(String searchTerm); // Búsqueda en nombre o apellido o nacionalidad
    
    // STATISTICS & AGGREGATION METHODS - NECESITA DE LA VISTA author_books_summary
    List<Author> findAuthorsWithBooks();
    List<Author> findAuthorsWithMoreThanXBooks(int minBooks);
    List<Author> findMostProlificAuthors(int limit); 
    
    // BATCH OPERATIONS
    int deleteAuthorsWithNoBooks();
    int updateNationality(String oldNationality, String newNationality);
    
    // VALIDATION & UTILITY METHODS
    boolean existsByFullName(String firstName, String lastName);
    int countByNationality(String nationality);
    List<String> findAllNationalities();
    
    // BIRTHDATE RELATED METHODS
    List<Author> findAuthorsBornBefore(java.time.LocalDate date);
    List<Author> findAuthorsBornAfter(java.time.LocalDate date);
    List<Author> findLivingAuthors(); // Nacidos después de 1900, por ejemplo
}