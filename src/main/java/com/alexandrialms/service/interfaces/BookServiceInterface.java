package com.alexandrialms.service.interfaces;

import com.alexandrialms.model.Author;
import com.alexandrialms.model.Book;
import com.alexandrialms.exception.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookServiceInterface {
    
    // CRUD OPERATIONS
    Optional<Book> createBook(Book book) throws ValidationException;
    Optional<Book> getBookById(int bookId) throws ValidationException;
    boolean updateBook(int bookId, Book book) throws ValidationException;
    boolean deleteBook(int bookId) throws ValidationException;
    
    // BASIC SEARCH OPERATIONS
    List<Book> getAllBooks();
    List<Book> searchBooks(String searchTerm) throws ValidationException;
    List<Book> getBooksByTitle(String title) throws ValidationException;
    Optional<Book> getBookByISBN(String isbn) throws ValidationException;
    List<Book> getBooksByPublicationYear(int year) throws ValidationException;
    List<Book> getBooksByPublicationYearRange(int startYear, int endYear) throws ValidationException;
    List<Book> getBooksByCategory(int categoryId) throws ValidationException;
    List<Book> getBooksByCategoryName(String categoryName) throws ValidationException;
    
    // ADVANCED SEARCH OPERATIONS
    List<Book> getBooksByTitleAndYear(String title, int year) throws ValidationException;
    List<Book> getBooksByCategoryAndYear(int categoryId, int year) throws ValidationException;
    
    // PAGINATION OPERATIONS
    List<Book> getBooksByTitlePaginated(String title, int limit, int offset) throws ValidationException;
    List<Book> getBooksByCategoryPaginated(int categoryId, int limit, int offset) throws ValidationException;
    List<Book> getAllBooksPaginated(int limit, int offset) throws ValidationException;
    
    // STATISTICS OPERATIONS
    int getBooksCountByCategory(int categoryId) throws ValidationException;
    int getBooksCountByPublicationYear(int year) throws ValidationException;
    Map<Integer, Integer> getBooksCountByYear() throws ValidationException;
    Map<Integer, Integer> getBooksCountByCategory() throws ValidationException;
    int getTotalBooksCount();
    
    // AVAILABILITY OPERATIONS
    List<Book> getAvailableBooks();
    List<Book> getUnavailableBooks();
    int getAvailableCopiesCount(int bookId) throws ValidationException;
    int getTotalCopiesCount(int bookId) throws ValidationException;
    boolean isBookAvailable(int bookId) throws ValidationException;
    
    // POPULARITY OPERATIONS
    List<Book> getMostBorrowedBooks(int limit) throws ValidationException;
    List<Book> getRecentlyAddedBooks(int limit) throws ValidationException;
    
    // AUTHOR RELATIONSHIP MANAGEMENT
    boolean addAuthorToBook(int bookId, int authorId) throws ValidationException;
    boolean removeAuthorFromBook(int bookId, int authorId) throws ValidationException;
    boolean setBookAuthors(int bookId, List<Integer> authorIds) throws ValidationException;
    List<Author> getBookAuthors(int bookId) throws ValidationException;
    
    // AUTHOR-BASED SEARCH OPERATIONS
    List<Book> getBooksByAuthor(int authorId) throws ValidationException;
    List<Book> getBooksByAuthorName(String authorName) throws ValidationException;
    List<Book> getBooksByAuthorAndCategory(int authorId, int categoryId) throws ValidationException;
    
    // ADVANCED AUTHOR SEARCH OPERATIONS
    List<Book> getBooksByMultipleAuthors(List<Integer> authorIds) throws ValidationException;
    List<Book> searchBooksWithAuthors(String searchTerm) throws ValidationException;
    
    // VALIDATION OPERATIONS
    boolean bookExistsByISBN(String isbn);
    boolean bookExistsByTitleAndYear(String title, int year);
    boolean bookHasAuthor(int bookId, int authorId) throws ValidationException;
    int getBooksCountByAuthor(int authorId) throws ValidationException;
    
    // BATCH OPERATIONS
    int deleteBooksByCategory(int categoryId) throws ValidationException;
    int updateBooksCategory(int oldCategoryId, int newCategoryId) throws ValidationException;
    int deleteBooksWithNoCopies() throws ValidationException;
}
