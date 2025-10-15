package com.alexandrialms.dao.interfaces;

import com.alexandrialms.model.Author;
import com.alexandrialms.model.Book;
import java.util.List;
import java.util.Map;

public interface BookDAOInterface extends GenericDAO<Book, Integer> {
        
    // EXISTING METHODS
    Book findByISBN(String isbn);
    List<Book> findByTitle(String partialTitle);
    
    // BASIC SEARCH METHODS
    List<Book> findByPublicationYear(int year);
    List<Book> findByPublicationYearRange(int startYear, int endYear);
    List<Book> findByCategory(int categoryId);
    List<Book> findByCategoryName(String categoryName);
    
    // ADVANCED SEARCH METHODS
    List<Book> searchBooks(String searchTerm); // Searches title and ISBN
    List<Book> findByTitleAndYear(String title, int year);
    List<Book> findByCategoryAndYear(int categoryId, int year);
    
    // PAGINATION METHODS 
    List<Book> findByTitlePaginated(String partialTitle, int limit, int offset); //Estos se usan para cuando en una web hay muchos resultados y se muestra estilo: « 1 2 3 4 5 ... »
    List<Book> findByCategoryPaginated(int categoryId, int limit, int offset);
    List<Book> findAllPaginated(int limit, int offset);
    
    // STATISTICS METHODS
    int countBooksByCategory(int categoryId);
    int countBooksByPublicationYear(int year);
    Map<Integer, Integer> getBooksCountByYear();
    Map<Integer, Integer> getBooksCountByCategory();
    
    // AVAILABILITY METHODS (REQUIRES JOIN WITH COPY AND LOAN TABLES)
    List<Book> findAvailableBooks();
    List<Book> findUnavailableBooks();
    int getAvailableCopiesCount(int bookId);
    int getTotalCopiesCount(int bookId);
    
    // POPULARITY METHODS (REQUIRES JOIN WITH LOAN TABLE)
    List<Book> findMostBorrowedBooks(int limit);
    List<Book> findRecentlyAddedBooks(int limit);
    
    // VALIDATION AND EXISTENCE METHODS
    boolean existsByISBN(String isbn);
    boolean existsByTitleAndYear(String title, int year);
    int countAllBooks();
    
    // BATCH OPERATIONS
    int deleteBooksByCategory(int categoryId);
    int updateBooksCategory(int oldCategoryId, int newCategoryId);
    int deleteBooksWithNoCopies();
    
    // --- METHODS USING book_author TABLE ---
    
    // AUTHOR RELATIONSHIP MANAGEMENT
    boolean addAuthorToBook(int bookId, int authorId); // INSERT into book_author
    boolean removeAuthorFromBook(int bookId, int authorId); // DELETE from book_author
    boolean setBookAuthors(int bookId, List<Integer> authorIds); // Manages multiple authors
    List<Author> getBookAuthors(int bookId); // JOIN with author via book_author
    
    // AUTHOR-BASED SEARCHES (REQUIRE JOIN WITH book_author)
    List<Book> findByAuthor(int authorId); // JOIN with book_author
    List<Book> findByAuthorName(String authorName); // JOIN with book_author + author
    List<Book> findByAuthorAndCategory(int authorId, int categoryId); // Double JOIN
    
    // RELATIONSHIP VALIDATION
    boolean hasAuthor(int bookId, int authorId); // SELECT from book_author
    int countBooksByAuthor(int authorId); // COUNT from book_author
    
    // ADVANCED AUTHOR SEARCHES
    List<Book> findByMultipleAuthors(List<Integer> authorIds); // Multiple authors
    List<Book> searchBooksWithAuthors(String searchTerm); // Search in title + author names
}