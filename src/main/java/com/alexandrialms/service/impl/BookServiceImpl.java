package com.alexandrialms.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.alexandrialms.dao.impl.AuthorDAO;
import com.alexandrialms.dao.impl.BookDAO;
import com.alexandrialms.dao.impl.CategoryDAO;
import com.alexandrialms.exception.ValidationException;
import com.alexandrialms.model.Author;
import com.alexandrialms.model.Book;
import com.alexandrialms.service.interfaces.BookServiceInterface;
import com.alexandrialms.util.ValidationHelper;

public class BookServiceImpl implements BookServiceInterface {
    private BookDAO bookDAO = new BookDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private AuthorDAO authorDAO = new AuthorDAO();

    @Override
    public Optional<Book> createBook(Book book) throws ValidationException {

        ValidationHelper.validateBookForInsert(book, bookDAO);

        if (bookDAO.insert(book)) {
            Book savedBook = bookDAO.findByISBN(book.getIsbn());
            return Optional.ofNullable(savedBook);
        } else {
            return Optional.empty();
        }
    }

@Override
public Optional<Book> getBookById(int bookId) throws ValidationException {
    if (bookId <= 0) {
        throw new ValidationException("Book ID must be positive: " + bookId);
    }
    Book book = bookDAO.findById(bookId);
    return Optional.ofNullable(book);
}

    @Override
    public boolean updateBook(int bookId, Book book) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }

        ValidationHelper.validateBookForUpdate(bookId, book, bookDAO);
        book.setBookID(bookId);
        return bookDAO.update(book);
    }

    @Override
    public boolean deleteBook(int bookId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        return bookDAO.delete(bookId);
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = bookDAO.findAll();
        return books;
    }

    @Override
    public List<Book> searchBooks(String searchTerm) throws ValidationException {
        if (!ValidationHelper.isValidString(searchTerm, 3)) {
            throw new ValidationException("Search term must be at least 3 characters long");
        }

        return bookDAO.searchBooks(searchTerm.trim());
    }

    @Override
    public List<Book> getBooksByTitle(String title) throws ValidationException {
        if (!ValidationHelper.isValidString(title, 3)) {
            throw new ValidationException("Title cannot be null or empty and at least 3 characters long");
        }
        List<Book> books = bookDAO.findByTitle(title);
        return books;
    }

    @Override
    public Optional<Book> getBookByISBN(String isbn) throws ValidationException {
        if (!ValidationHelper.isValidISBN(isbn)) {
            throw new ValidationException("Invalid ISBN format: " + isbn);
        }
        Optional<Book> book = Optional.ofNullable(bookDAO.findByISBN(isbn));
        return book;
    }

    @Override
    public List<Book> getBooksByPublicationYear(int year) throws ValidationException {
        if (year < 500 || year > java.time.Year.now().getValue()) {
            throw new ValidationException("Publication year must be between 500 and current year: " + year);
        }

        return bookDAO.findByPublicationYear(year);
    }

    @Override
    public List<Book> getBooksByPublicationYearRange(int startYear, int endYear) throws ValidationException {
        if (!ValidationHelper.isValidDateRange(LocalDate.of(startYear, 1, 1), LocalDate.of(endYear, 12, 31))) {
            throw new ValidationException("Invalid year range: " + startYear + " - " + endYear);
        }
        return bookDAO.findByPublicationYearRange(startYear, endYear);
    }

    @Override
    public List<Book> getBooksByCategory(int categoryId) throws ValidationException {
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            throw new ValidationException("Invalid category ID: " + categoryId);
        }
        List<Book> books = bookDAO.findByCategory(categoryId);
        return books;
    }

    @Override
    public List<Book> getBooksByCategoryName(String categoryName) throws ValidationException {
        if (!ValidationHelper.isValidString(categoryName, 3)) {
            throw new ValidationException("Category name must be at least 3 characters long");
        }
        List<Book> books = bookDAO.findByCategoryName(categoryName.trim());
        return books;
    }

    @Override
    public List<Book> getBooksByTitleAndYear(String title, int year) throws ValidationException {
        if (!ValidationHelper.isValidString(title, 3)) {
            throw new ValidationException("Title cannot be null or empty and at least 3 characters long");
        }
        if (year < 500 || year > java.time.Year.now().getValue()) {
            throw new ValidationException("Publication year must be between 500 and current year: " + year);
        }
        return bookDAO.findByTitleAndYear(title, year);
    }

    @Override
    public List<Book> getBooksByCategoryAndYear(int categoryId, int year) throws ValidationException {
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            throw new ValidationException("Invalid category ID: " + categoryId);
        }
        if (year < 500 || year > java.time.Year.now().getValue()) {
            throw new ValidationException("Publication year must be between 500 and current year: " + year);
        }
        return bookDAO.findByCategoryAndYear(categoryId, year);
    }

    @Override
    public List<Book> getBooksByTitlePaginated(String title, int limit, int offset) throws ValidationException {
        if (!ValidationHelper.isValidString(title, 3)) {
            throw new ValidationException("Title cannot be null or empty and at least 3 characters long");
        }
        if (limit <= 0 || offset < 0) {
            throw new ValidationException("Limit must be greater than 0 and offset cannot be negative");
        }
        return bookDAO.findByTitlePaginated(title, limit, offset);
    }

    @Override
    public List<Book> getBooksByCategoryPaginated(int categoryId, int limit, int offset) throws ValidationException {
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            throw new ValidationException("Invalid category ID: " + categoryId);
        }
        if (limit <= 0 || offset < 0) {
            throw new ValidationException("Limit must be greater than 0 and offset cannot be negative");
        }
        return bookDAO.findByCategoryPaginated(categoryId, limit, offset);
    }

    @Override
    public List<Book> getAllBooksPaginated(int limit, int offset) throws ValidationException {
        if (limit <= 0 || offset < 0) {
            throw new ValidationException("Limit must be greater than 0 and offset cannot be negative");
        }
        return bookDAO.findAllPaginated(limit, offset);
    }

    @Override
    public int getBooksCountByCategory(int categoryId) throws ValidationException {
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            throw new ValidationException("Invalid category ID: " + categoryId);
        }
        return bookDAO.countBooksByCategory(categoryId);
    }

    @Override
    public int getBooksCountByPublicationYear(int year) throws ValidationException {
        if (year < 500 || year > java.time.Year.now().getValue()) {
            throw new ValidationException("Publication year must be between 500 and current year: " + year);
        }
        return bookDAO.countBooksByPublicationYear(year);
    }

    @Override
    public Map<Integer, Integer> getBooksCountByYear() throws ValidationException {
        return bookDAO.getBooksCountByYear();
    }

    @Override
    public Map<Integer, Integer> getBooksCountByCategory() throws ValidationException {
        return bookDAO.getBooksCountByCategory();
    }

    @Override
    public int getTotalBooksCount() {
        return bookDAO.countAllBooks();
    }

    @Override
    public List<Book> getAvailableBooks() throws ValidationException {
        return bookDAO.findAvailableBooks();
    }

    @Override
    public List<Book> getUnavailableBooks() throws ValidationException {
        return bookDAO.findUnavailableBooks();
    }

    @Override
    public int getAvailableCopiesCount(int bookId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        return bookDAO.getAvailableCopiesCount(bookId);
    }

    @Override
    public int getTotalCopiesCount(int bookId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        return bookDAO.getTotalCopiesCount(bookId);
    }

    @Override
    public boolean isBookAvailable(int bookId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        return bookDAO.getAvailableCopiesCount(bookId) != 0;
    }

    @Override
    public List<Book> getMostBorrowedBooks(int limit) throws ValidationException {
        if (limit <= 0) {
            throw new ValidationException("Limit must be greater than 0");
        }
        return bookDAO.findMostBorrowedBooks(limit);
    }

    @Override
    public List<Book> getRecentlyAddedBooks(int limit) throws ValidationException {
        if (limit <= 0) {
            throw new ValidationException("Limit must be greater than 0");
        }
        return bookDAO.findRecentlyAddedBooks(limit);
    }

@Override
public boolean addAuthorToBook(int bookId, int authorId) throws ValidationException {
    if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
        throw new ValidationException("Invalid book ID: " + bookId);
    }
    
    if (!ValidationHelper.isValidAuthorID(authorId, authorDAO)) {
        throw new ValidationException("Invalid author ID: " + authorId);
    }
    
    if (bookDAO.hasAuthor(bookId, authorId)) {
        throw new ValidationException("Author " + authorId + " is already associated with book " + bookId);
    }
    
    return bookDAO.addAuthorToBook(bookId, authorId);
}

    @Override
    public boolean removeAuthorFromBook(int bookId, int authorId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        
        if (!ValidationHelper.isValidAuthorID(authorId, authorDAO)) {
            throw new ValidationException("Invalid author ID: " + authorId);
        }
        
        if (!bookDAO.hasAuthor(bookId, authorId)) {
            throw new ValidationException("Author " + authorId + " is not associated with book " + bookId);
        }
        
        return bookDAO.removeAuthorFromBook(bookId, authorId);
    }

    @Override
    public boolean setBookAuthors(int bookId, List<Integer> authorIds) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }

        for (int authorId : authorIds) {
            if (!ValidationHelper.isValidAuthorID(authorId, authorDAO)) {
                throw new ValidationException("Invalid author ID: " + authorId);
            }
        }

        return bookDAO.setBookAuthors(bookId, authorIds);
    }

    @Override
    public List<Author> getBookAuthors(int bookId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        return bookDAO.getBookAuthors(bookId);
    }

    @Override
    public List<Book> getBooksByAuthor(int authorId) throws ValidationException {
        if (!ValidationHelper.isValidAuthorID(authorId, authorDAO)) {
            throw new ValidationException("Invalid author ID: " + authorId);
        }
        List<Book> books = bookDAO.findByAuthor(authorId);
        return books;
    }

    @Override
    public List<Book> getBooksByAuthorName(String authorName) throws ValidationException {
        if (!ValidationHelper.isValidString(authorName, 3)) {
            throw new ValidationException("Author name must be at least 3 characters long");
        }
        List<Book> books = bookDAO.findByAuthorName(authorName.trim());
        return books;
    }

    @Override
    public List<Book> getBooksByAuthorAndCategory(int authorId, int categoryId) throws ValidationException {
        if (!ValidationHelper.isValidAuthorID(authorId, authorDAO)) {
            throw new ValidationException("Invalid author ID: " + authorId);
        }
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            throw new ValidationException("Invalid category ID: " + categoryId);
        }
        return bookDAO.findByAuthorAndCategory(authorId, categoryId);
    }

    @Override
    public List<Book> getBooksByMultipleAuthors(List<Integer> authorIds) throws ValidationException {
        for (int authorId : authorIds) {
            if (!ValidationHelper.isValidAuthorID(authorId, authorDAO)) {
                throw new ValidationException("Invalid author ID: " + authorId);
            }
        }
        return bookDAO.findByMultipleAuthors(authorIds);
    }

    @Override
    public List<Book> searchBooksWithAuthors(String searchTerm) throws ValidationException {
        if (!ValidationHelper.isValidString(searchTerm, 3)) {
            throw new ValidationException("Search term must be at least 3 characters long");
        }

        return bookDAO.searchBooksWithAuthors(searchTerm.trim());
    }

    @Override
    public boolean bookExistsByISBN(String isbn) {
        if (!ValidationHelper.isValidISBN(isbn)) {
            return false;
        }
        return bookDAO.existsByISBN(isbn);
    }

    @Override
    public boolean bookExistsByTitleAndYear(String title, int year) {
        if (!ValidationHelper.isValidString(title, 3)) {
            return false;
        }
        if (year < 500 || year > java.time.Year.now().getValue()) {
            return false;
        }
        return bookDAO.existsByTitleAndYear(title, year);
    }

    @Override
    public boolean bookHasAuthor(int bookId, int authorId) throws ValidationException {
        if (!ValidationHelper.isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("Invalid book ID: " + bookId);
        }
        
        if (!ValidationHelper.isValidAuthorID(authorId, authorDAO)) {
            throw new ValidationException("Invalid author ID: " + authorId);
        }
        
        return bookDAO.hasAuthor(bookId, authorId);
    }

    @Override
    public int getBooksCountByAuthor(int authorId) throws ValidationException {
        if (!ValidationHelper.isValidAuthorID(authorId, authorDAO)) {
            throw new ValidationException("Invalid author ID: " + authorId);
        }
        return bookDAO.countBooksByAuthor(authorId);
    }

    @Override
    public int deleteBooksByCategory(int categoryId) throws ValidationException {
        if (!ValidationHelper.isValidCategoryID(categoryId, categoryDAO)) {
            throw new ValidationException("Invalid category ID: " + categoryId);
        }
        return bookDAO.deleteBooksByCategory(categoryId);
    }

    @Override
    public int updateBooksCategory(int oldCategoryId, int newCategoryId) throws ValidationException {
        if (!ValidationHelper.isValidCategoryID(oldCategoryId, categoryDAO)) {
            throw new ValidationException("Invalid old category ID: " + oldCategoryId);
        }
        if (!ValidationHelper.isValidCategoryID(newCategoryId, categoryDAO)) {
            throw new ValidationException("Invalid new category ID: " + newCategoryId);
        }
        return bookDAO.updateBooksCategory(oldCategoryId, newCategoryId);
    }

    @Override
    public int deleteBooksWithNoCopies() throws ValidationException {
        return bookDAO.deleteBooksWithNoCopies();
    }

    public BookServiceImpl(BookDAO bookDAO, AuthorDAO authorDAO, CategoryDAO categoryDAO) {
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
        this.categoryDAO = categoryDAO;
    }
    
    public BookServiceImpl() {
        this.bookDAO = new BookDAO();
        this.authorDAO = new AuthorDAO();
        this.categoryDAO = new CategoryDAO();
    }
}
