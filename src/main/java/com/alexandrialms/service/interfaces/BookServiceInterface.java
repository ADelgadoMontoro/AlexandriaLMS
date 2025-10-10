package com.alexandrialms.service.interfaces;

import com.alexandrialms.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookServiceInterface {
    
    Book createBook(Book book);
    Book createBookWithAuthor(Book book, int authorId);
    Optional<Book> findBookById(int bookId);
    List<Book> findAllBooks();
    Optional<Book> findBookByIsbn(String isbn);
    List<Book> findBooksByTitle(String title);
    List<Book> findBooksByCategory(int categoryId);
    List<Book> findBooksByAuthor(int authorId);
    List<Book> findBooksByPublicationYear(int year);
    List<Book> findBooksByPublicationYearRange(int startYear, int endYear);
    List<Book> searchBooks(String searchTerm);
    List<Book> findBooksByTitleContaining(String partialTitle);
    Optional<Book> updateBook(Book book);
    boolean updateBookCategory(int bookId, int categoryId);
    boolean deleteBook(int bookId);
    int deleteBooksWithNoCopies();
    boolean bookExists(int bookId);
    boolean isbnExists(String isbn);
    boolean isBookAvailableForLoan(int bookId);
    int countBooks();
    int countBooksByCategory(int categoryId);
    int countBooksByAuthor(int authorId);
    int getTotalCopiesCount(int bookId);
    int getAvailableCopiesCount(int bookId);
    boolean hasAvailableCopies(int bookId);
}
