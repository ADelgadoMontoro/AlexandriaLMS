package com.alexandrialms.util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alexandrialms.dao.impl.AuthorDAO;
import com.alexandrialms.dao.impl.BookDAO;
import com.alexandrialms.dao.impl.CategoryDAO;
import com.alexandrialms.dao.impl.CopyDAO;
import com.alexandrialms.dao.impl.LoanDAO;
import com.alexandrialms.dao.impl.PasswordDAO;
import com.alexandrialms.dao.impl.UserDAO;
import com.alexandrialms.exception.ValidationException;
import com.alexandrialms.model.Author;
import com.alexandrialms.model.Book;
import com.alexandrialms.model.Category;
import com.alexandrialms.model.Copy;
import com.alexandrialms.model.CopyStatus;
import com.alexandrialms.model.LibraryRole;
import com.alexandrialms.model.User;

/**
 * Centralized validation helper for AlexandriaLMS application.
 * Provides comprehensive validation methods for all entity models and business
 * rules.
 * Implements fail-fast validation pattern with detailed error messages.
 */
public class ValidationHelper {
    // =========================================================================
    // USER MODEL VALIDATIONS
    // =========================================================================

    /**
     * Validates basic user entity constraints.
     * 
     * @param user the user to validate
     * @throws ValidationException if user data is invalid
     */
    public static void validateUser(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("user", "USER_NULL", "User cannot be null");
        }

        // Validate name fields
        if (!isValidString(user.getFirstName(), 2)) {
            throw new ValidationException("firstName", "INVALID_FIRST_NAME",
                    "First name must be at least 2 characters long");
        }

        if (user.getFirstName().length() > 50) {
            throw new ValidationException("firstName", "FIRST_NAME_TOO_LONG",
                    "First name cannot exceed 50 characters");
        }

        if (!isValidString(user.getLastName(), 2)) {
            throw new ValidationException("lastName", "INVALID_LAST_NAME",
                    "Last name must be at least 2 characters long");
        }

        if (user.getLastName().length() > 50) {
            throw new ValidationException("lastName", "LAST_NAME_TOO_LONG",
                    "Last name cannot exceed 50 characters");
        }

        // Validate email
        if (!isValidEmail(user.getEmail())) {
            throw new ValidationException("email", "INVALID_EMAIL",
                    "Valid email address is required");
        }

        // Validate phone (optional but must be valid if provided)
        if (user.getPhone() != null && !user.getPhone().isEmpty() && !isValidPhone(user.getPhone())) {
            throw new ValidationException("phone", "INVALID_PHONE",
                    "Phone number format is invalid");
        }

        // Validate address (optional but must not exceed limit if provided)
        if (user.getAddress() != null && user.getAddress().length() > 200) {
            throw new ValidationException("address", "ADDRESS_TOO_LONG",
                    "Address cannot exceed 200 characters");
        }

        // Validate role
        if (user.getRole() == null) {
            throw new ValidationException("role", "INVALID_ROLE",
                    "User role is required");
        }

        // Validate registration date (should not be in future)
        if (user.getRegistrationDate() != null && user.getRegistrationDate().isAfter(LocalDateTime.now())) {
            throw new ValidationException("registrationDate", "FUTURE_REGISTRATION_DATE",
                    "Registration date cannot be in the future");
        }
    }

    /**
     * Validates user for insertion including business rules and uniqueness.
     * 
     * @param user    the user to validate
     * @param userDAO user data access object
     * @throws ValidationException if user data is invalid or duplicate exists
     */
    public static void validateUserForInsert(User user, UserDAO userDAO) throws ValidationException {
        validateUser(user);

        try {
            if (userDAO.existsByEmail(user.getEmail())) {
                throw new ValidationException("email", "DUPLICATE_EMAIL",
                        "A user with email '" + user.getEmail() + "' already exists");
            }
        } catch (SQLException e) {
            throw new ValidationException("email", "DATABASE_ERROR",
                    "Error checking email uniqueness: " + e.getMessage());
        }

        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            if (!isValidPhone(user.getPhone())) {
                throw new ValidationException("phone", "INVALID_PHONE",
                        "Phone number format is invalid");
            }

            try {
                if (userDAO.existsByPhone(user.getPhone())) {
                    throw new ValidationException("phone", "DUPLICATE_PHONE",
                            "A user with phone '" + user.getPhone() + "' already exists");
                }
            } catch (SQLException e) {
                throw new ValidationException("phone", "DATABASE_ERROR",
                        "Error checking phone uniqueness: " + e.getMessage());
            }
        }

        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDateTime.now());
        }

        if (!user.isActive()) {
            user.setActive(true);
        }
    }

    /**
     * Validates user for update including existence and uniqueness checks.
     * 
     * @param user    the user to validate
     * @param userDAO user data access object
     * @throws ValidationException if user data is invalid or duplicate exists
     */
    public static void validateUserForUpdate(User user, UserDAO userDAO) throws ValidationException {
        validateUser(user);
        if (!isValidUserID(user.getUserID(), userDAO)) {
            throw new ValidationException("userId", "USER_NOT_FOUND",
                    "User with ID " + user.getUserID() + " does not exist");
        }
        try {
            User existingUserWithEmail = userDAO.findByEmail(user.getEmail());
            if (existingUserWithEmail != null && existingUserWithEmail.getUserID() != user.getUserID()) {
                throw new ValidationException("email", "DUPLICATE_EMAIL",
                        "Another user with email '" + user.getEmail() + "' already exists");
            }
        } catch (SQLException e) {
            throw new ValidationException("email", "DATABASE_ERROR",
                    "Error checking email uniqueness: " + e.getMessage());
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            if (!isValidPhone(user.getPhone())) {
                throw new ValidationException("phone", "INVALID_PHONE",
                        "Phone number format is invalid");
            }
        }
    }

    /**
     * Validates user for deletion including existence checks.
     * 
     * @param userId  the user ID to validate
     * @param userDAO user data access object
     * @throws ValidationException if user does not exist or has active loans
     */
    public static void validateUserDeletion(int userId, UserDAO userDAO) throws ValidationException {
        if (!isValidUserID(userId, userDAO)) {
            throw new ValidationException("userId", "USER_NOT_FOUND",
                    "User with ID " + userId + " does not exist");
        }

        // Check if user has active loans
        try {
            int activeLoansCount = userDAO.countActiveLoansByUser(userId);
            if (activeLoansCount > 0) {
                throw new ValidationException("userId", "USER_HAS_ACTIVE_LOANS",
                        "Cannot delete user with " + activeLoansCount + " active loans");
            }
        } catch (SQLException e) {
            throw new ValidationException("userId", "DATABASE_ERROR",
                    "Error checking user's active loans: " + e.getMessage());
        }
    }

    /**
     * Validates user ID parameter.
     * 
     * @param userId the user ID to validate
     * @throws ValidationException if user ID is invalid
     */
    public static void validateUserId(int userId) throws ValidationException {
        if (userId <= 0) {
            throw new ValidationException("userId", "INVALID_USER_ID",
                    "User ID must be greater than 0");
        }
    }

    /**
     * Validates search term for user searches.
     * 
     * @param searchTerm the search term to validate
     * @throws ValidationException if search term is invalid
     */
    public static void validateUserSearchTerm(String searchTerm) throws ValidationException {
        if (!isValidString(searchTerm, 3)) {
            throw new ValidationException("searchTerm", "INVALID_SEARCH_TERM",
                    "Search term must be at least 3 characters long");
        }

        if (searchTerm.length() > 100) {
            throw new ValidationException("searchTerm", "SEARCH_TERM_TOO_LONG",
                    "Search term cannot exceed 100 characters");
        }
    }

    /**
     * Validates role parameter.
     * 
     * @param role the role to validate
     * @throws ValidationException if role is invalid
     */
    public static void validateUserRole(String role) throws ValidationException {
        if (!isValidString(role, 1)) {
            throw new ValidationException("role", "INVALID_ROLE",
                    "Role is required");
        }

        try {
            LibraryRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("role", "INVALID_ROLE_VALUE",
                    "Role must be one of: " + Arrays.toString(LibraryRole.values()));
        }
    }

    /**
     * Validates date range for registration date searches.
     * 
     * @param startDate the start date to validate
     * @param endDate   the end date to validate
     * @throws ValidationException if date range is invalid
     */
    public static void validateRegistrationDateRange(LocalDate startDate, LocalDate endDate)
            throws ValidationException {
        if (startDate == null || endDate == null) {
            throw new ValidationException("dateRange", "INVALID_DATE_RANGE",
                    "Both start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new ValidationException("dateRange", "INVALID_DATE_RANGE",
                    "Start date cannot be after end date");
        }

        if (startDate.isAfter(LocalDate.now())) {
            throw new ValidationException("startDate", "FUTURE_START_DATE",
                    "Start date cannot be in the future");
        }
    }

    /**
     * Validates pagination parameters.
     * 
     * @param limit  the limit to validate
     * @param offset the offset to validate
     * @throws ValidationException if pagination parameters are invalid
     */
    public static void validatePaginationParameters(int limit, int offset) throws ValidationException {
        if (limit <= 0) {
            throw new ValidationException("limit", "INVALID_LIMIT",
                    "Limit must be greater than 0");
        }

        if (offset < 0) {
            throw new ValidationException("offset", "INVALID_OFFSET",
                    "Offset cannot be negative");
        }
    }

    // =========================================================================
    // COPY MODEL VALIDATIONS
    // =========================================================================

    /**
     * Validates basic copy entity constraints.
     * 
     * @param copy the copy to validate
     * @throws ValidationException if copy data is invalid
     */
    public static void validateCopy(Copy copy) throws ValidationException {
        if (copy == null) {
            throw new ValidationException("copy", "COPY_NULL", "Copy cannot be null");
        }

        // Validate associated book
        if (copy.getBook_id() <= 0) {
            throw new ValidationException("book_id", "INVALID_BOOK_ID",
                    "Copy must be associated with a valid book");
        }

        // Validate internal code
        if (!isValidString(copy.getInternal_code(), 1)) {
            throw new ValidationException("internal_code", "INVALID_INTERNAL_CODE",
                    "Internal code is required");
        }

        if (copy.getInternal_code().length() > 50) {
            throw new ValidationException("internal_code", "INTERNAL_CODE_TOO_LONG",
                    "Internal code cannot exceed 50 characters");
        }

        // Validate status (CopyStatus enum)
        if (copy.getStatus() == null) {
            throw new ValidationException("status", "INVALID_STATUS",
                    "Copy status is required");
        }
    }

    /**
     * Validates copy for insertion including business rules and uniqueness.
     * 
     * @param copy    the copy to validate
     * @param copyDAO copy data access object
     * @param bookDAO book data access object
     * @throws ValidationException if copy data is invalid or duplicate exists
     */
    public static void validateCopyForInsert(Copy copy, CopyDAO copyDAO, BookDAO bookDAO) throws ValidationException {
        validateCopy(copy);

        // Validate that the book exists
        if (!isValidBookID(copy.getBook_id(), bookDAO)) {
            throw new ValidationException("book_id", "BOOK_NOT_FOUND",
                    "Book with ID " + copy.getBook_id() + " does not exist");
        }

        // Validate that no other copy has the same internal code
        if (copyDAO.existsByInternalCode(copy.getInternal_code())) {
            throw new ValidationException("internal_code", "DUPLICATE_INTERNAL_CODE",
                    "A copy with internal code '" + copy.getInternal_code() + "' already exists");
        }
    }

    /**
     * Validates copy for update including existence and uniqueness checks.
     * 
     * @param copy    the copy to validate
     * @param copyDAO copy data access object
     * @param bookDAO book data access object
     * @throws ValidationException if copy data is invalid or duplicate exists
     */
    public static void validateCopyForUpdate(Copy copy, CopyDAO copyDAO, BookDAO bookDAO) throws ValidationException {
        validateCopy(copy);

        // Validate that the copy exists
        if (!isValidCopyID(copy.getCopyID(), copyDAO)) {
            throw new ValidationException("copyId", "COPY_NOT_FOUND",
                    "Copy with ID " + copy.getCopyID() + " does not exist");
        }

        // Validate that the book exists
        if (!isValidBookID(copy.getBook_id(), bookDAO)) {
            throw new ValidationException("book_id", "BOOK_NOT_FOUND",
                    "Book with ID " + copy.getBook_id() + " does not exist");
        }

        Copy existingCopyWithInternalCode = copyDAO.findByInternalCode(copy.getInternal_code());
        if (existingCopyWithInternalCode != null && existingCopyWithInternalCode.getCopyID() != copy.getCopyID()) {
            throw new ValidationException("internal_code", "DUPLICATE_INTERNAL_CODE",
                    "Another copy with internal code '" + copy.getInternal_code() + "' already exists");
        }
    }

    /**
     * Validates copy for deletion including existence checks.
     * 
     * @param copyId  the copy ID to validate
     * @param copyDAO copy data access object
     * @throws ValidationException if copy does not exist or has active loans
     */
    public static void validateCopyDeletion(int copyId, CopyDAO copyDAO) throws ValidationException {
        if (!isValidCopyID(copyId, copyDAO)) {
            throw new ValidationException("copyId", "COPY_NOT_FOUND",
                    "Copy with ID " + copyId + " does not exist");
        }
    }

    /**
     * Validates internal code format and constraints.
     * 
     * @param internalCode the internal code to validate
     * @throws ValidationException if internal code is invalid
     */
    public static void validateInternalCode(String internalCode) throws ValidationException {
        if (!isValidString(internalCode, 1)) {
            throw new ValidationException("internal_code", "INVALID_INTERNAL_CODE",
                    "Internal code is required");
        }

        if (internalCode.length() > 50) {
            throw new ValidationException("internal_code", "INTERNAL_CODE_TOO_LONG",
                    "Internal code cannot exceed 50 characters");
        }
    }

    public static void validateCopyStatus(String status) throws ValidationException {
        if (!isValidString(status, 1)) {
            throw new ValidationException("status", "INVALID_STATUS", "Copy status is required");
        }

        try {
            CopyStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("status", "INVALID_STATUS_VALUE",
                    "Copy status must be one of: " + Arrays.toString(CopyStatus.values()));
        }
    }

    // =========================================================================
    // CATEGORY MODEL VALIDATIONS
    // =========================================================================

    /**
     * Validates basic category entity constraints.
     * 
     * @param category the category to validate
     * @throws ValidationException if category data is invalid
     */
    public static void validateCategory(Category category) throws ValidationException {
        if (category == null) {
            throw new ValidationException("category", "CATEGORY_NULL", "Category cannot be null");
        }

        if (!isValidString(category.getName(), 2)) {
            throw new ValidationException("name", "INVALID_CATEGORY_NAME",
                    "Category name is required and must be at least 2 characters long");
        }

        if (category.getName().length() > 100) {
            throw new ValidationException("name", "CATEGORY_NAME_TOO_LONG",
                    "Category name cannot exceed 100 characters");
        }

        // Validate description
        if (!isValidString(category.getDescription(), 1)) {
            throw new ValidationException("description", "INVALID_CATEGORY_DESCRIPTION",
                    "Category description is required");
        }

        if (category.getDescription().length() > 500) {
            throw new ValidationException("description", "CATEGORY_DESCRIPTION_TOO_LONG",
                    "Category description cannot exceed 500 characters");
        }
    }

    /**
     * Validates category for insertion including uniqueness checks.
     * 
     * @param category    the category to validate
     * @param categoryDAO category data access object
     * @throws ValidationException if category data is invalid or duplicate exists
     */
    public static void validateCategoryForInsert(Category category, CategoryDAO categoryDAO)
            throws ValidationException {
        validateCategory(category);

        if (categoryDAO.existsByName(category.getName())) {
            throw new ValidationException("name", "DUPLICATE_CATEGORY_NAME",
                    "A category with name '" + category.getName() + "' already exists");
        }
    }

    /**
     * Validates category for update including existence and uniqueness checks.
     * 
     * @param category    the category to validate
     * @param categoryDAO category data access object
     * @throws ValidationException if category data is invalid or duplicate exists
     */
    public static void validateCategoryForUpdate(Category category, CategoryDAO categoryDAO)
            throws ValidationException {
        validateCategory(category);

        if (!isValidCategoryID(category.getCategoryID(), categoryDAO)) {
            throw new ValidationException("categoryId", "CATEGORY_NOT_FOUND",
                    "Category with ID " + category.getCategoryID() + " does not exist");
        }

        Category existingCategoryWithName = categoryDAO.findByName(category.getName());
        if (existingCategoryWithName != null && existingCategoryWithName.getCategoryID() != category.getCategoryID()) {
            throw new ValidationException("name", "DUPLICATE_CATEGORY_NAME",
                    "Another category with name '" + category.getName() + "' already exists");
        }
    }

    /**
     * Validates category for deletion including existence and business rules.
     * 
     * @param categoryId  the category ID to validate
     * @param categoryDAO category data access object
     * @throws ValidationException if category does not exist or has associated
     *                             books
     */
    public static void validateCategoryDeletion(int categoryId, CategoryDAO categoryDAO) throws ValidationException {
        if (!isValidCategoryID(categoryId, categoryDAO)) {
            throw new ValidationException("categoryId", "CATEGORY_NOT_FOUND",
                    "Category with ID " + categoryId + " does not exist");
        }

        int booksCount = categoryDAO.countBooksInCategory(categoryId);
        if (booksCount > 0) {
            throw new ValidationException("categoryId", "CATEGORY_HAS_BOOKS",
                    "Cannot delete category with ID " + categoryId + " - it has " + booksCount + " books associated");
        }
    }

    // =========================================================================
    // BOOK MODEL VALIDATIONS
    // =========================================================================

    /**
     * Validates book for update including existence and uniqueness checks.
     * 
     * @param bookId  the book ID to update
     * @param book    the book data to validate
     * @param bookDAO book data access object
     * @throws ValidationException if book data is invalid or duplicate ISBN exists
     */
    public static void validateBookForUpdate(int bookId, Book book, BookDAO bookDAO) throws ValidationException {
        if (book == null) {
            throw new ValidationException("book", "BOOK_NULL", "Book cannot be null");
        }

        if (!isValidBookID(bookId, bookDAO)) {
            throw new ValidationException("bookId", "BOOK_NOT_FOUND",
                    "Book with ID " + bookId + " does not exist");
        }

        validateBook(book);

        Book existingBookWithISBN = bookDAO.findByISBN(book.getIsbn());
        if (existingBookWithISBN != null && existingBookWithISBN.getBookID() != bookId) {
            throw new ValidationException("isbn", "DUPLICATE_ISBN",
                    "Another book with ISBN " + book.getIsbn() + " already exists");
        }
    }

    /**
     * Validates book for insertion including uniqueness checks.
     * 
     * @param book    the book to validate
     * @param bookDAO book data access object
     * @throws ValidationException if book data is invalid or duplicate ISBN exists
     */
    public static void validateBookForInsert(Book book, BookDAO bookDAO) throws ValidationException {
        if (book == null) {
            throw new ValidationException("book", "BOOK_NULL", "Book cannot be null");
        }

        validateBook(book);

        if (bookDAO.existsByISBN(book.getIsbn())) {
            throw new ValidationException("isbn", "DUPLICATE_ISBN",
                    "A book with ISBN " + book.getIsbn() + " already exists");
        }
    }

    /**
     * Validates basic book entity constraints.
     * 
     * @param book the book to validate
     * @throws ValidationException if book data is invalid
     */
    public static void validateBook(Book book) throws ValidationException {
        if (book == null) {
            throw new ValidationException("book", "BOOK_NULL", "Book cannot be null");
        }

        if (!isValidString(book.getTitle(), 1)) {
            throw new ValidationException("title", "INVALID_TITLE",
                    "Title is required and cannot be empty");
        }

        if (book.getTitle().length() > 500) {
            throw new ValidationException("title", "TITLE_TOO_LONG",
                    "Title cannot exceed 500 characters");
        }

        if (!isValidISBN(book.getIsbn())) {
            throw new ValidationException("isbn", "INVALID_ISBN",
                    "ISBN must be 10 or 13 digits with valid format");
        }

        if (book.getPubYear() < 500 || book.getPubYear() > Year.now().getValue()) {
            throw new ValidationException("pubYear", "INVALID_PUBLICATION_YEAR",
                    "Publication year must be between 500 and current year");
        }

        if (book.getCategoryId() <= 0) {
            throw new ValidationException("categoryId", "INVALID_CATEGORY_ID",
                    "Category ID must be positive");
        }
    }

    // =========================================================================
    // AUTHOR MODEL VALIDATIONS
    // =========================================================================

    /**
     * Validates basic author entity constraints.
     * 
     * @param author the author to validate
     * @throws ValidationException if author data is invalid
     */
    public static void validateAuthor(Author author) {
        if (author == null) {
            throw new ValidationException("author", "AUTHOR_NULL", "Author cannot be null");
        }

        if (!isValidString(author.getFirstName(), 2)) {
            throw new ValidationException("firstName", "INVALID_FIRST_NAME",
                    "First name is required and must be at least 2 characters long");
        }

        if (!isValidString(author.getLastName(), 2)) {
            throw new ValidationException("lastName", "INVALID_LAST_NAME",
                    "Last name is required and must be at least 2 characters long");
        }

        if (author.getBirthDate() != null && author.getBirthDate().isAfter(LocalDate.now())) {
            throw new ValidationException("birthDate", "FUTURE_BIRTH_DATE",
                    "Birth date cannot be in the future");
        }

        if (author.getBirthDate() != null && author.getBirthDate().isAfter(LocalDate.now().minusYears(10))) {
            throw new ValidationException("birthDate", "AUTHOR_TOO_YOUNG",
                    "Author must be at least 10 years old");
        }
    }

    // =========================================================================
    // ISBN VALIDATION METHODS
    // =========================================================================

    /**
     * Validates ISBN format and control digit.
     * 
     * @param isbn the ISBN to validate
     * @return true if ISBN is valid, false otherwise
     */
    public static boolean isValidISBN(String isbn) {
        if (isbn == null)
            return false;
        String isbn10Regex = "^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
        String isbn13Regex = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$";
        Pattern pattern10 = Pattern.compile(isbn10Regex);
        Matcher matcher10 = pattern10.matcher(isbn);
        Pattern pattern13 = Pattern.compile(isbn13Regex);
        Matcher matcher13 = pattern13.matcher(isbn);

        return (matcher10.matches() || matcher13.matches()) && validateControlDigitISBN(isbn);
    }

    /**
     * Validates ISBN-13 control digit.
     * 
     * @param isbn13 the ISBN-13 to validate
     * @return true if control digit is valid, false otherwise
     */
    private static boolean validateISBN13(String isbn13) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            char c = isbn13.charAt(i);
            if (!Character.isDigit(c))
                return false;
            int digit = c - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int expectedControl = (10 - (sum % 10)) % 10;
        int actualControl = isbn13.charAt(12) - '0';
        return expectedControl == actualControl;
    }

    /**
     * Validates ISBN-10 control digit.
     * 
     * @param isbn10 the ISBN-10 to validate
     * @return true if control digit is valid, false otherwise
     */
    private static boolean validateISBN10(String isbn10) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            char c = isbn10.charAt(i);
            if (!Character.isDigit(c))
                return false;
            sum += (c - '0') * (i + 1);
        }

        char last = isbn10.charAt(9);
        int control;
        if (last == 'X' || last == 'x') {
            control = 10;
        } else if (Character.isDigit(last)) {
            control = last - '0';
        } else {
            return false;
        }

        return sum % 11 == control;
    }

    /**
     * Validates ISBN control digit according to ISBN-10 or ISBN-13 standards.
     * 
     * @param isbn the ISBN to validate
     * @return true if control digit is valid, false otherwise
     */
    public static boolean validateControlDigitISBN(String isbn) {
        isbn = cleanISBN(isbn);
        if (isbn.length() == 10)
            return validateISBN10(isbn);
        if (isbn.length() == 13)
            return validateISBN13(isbn);
        return false;
    }

    /**
     * Cleans ISBN string by removing prefixes and formatting characters.
     * 
     * @param isbn the ISBN to clean
     * @return cleaned ISBN string
     */
    private static String cleanISBN(String isbn) {
        if (isbn == null)
            return "";
        return isbn.replaceAll("(?i)ISBN(?:-1[03])?:?", "")
                .replaceAll("[-\\s]", "");
    }

    // =========================================================================
    // ENTITY ID VALIDATION METHODS
    // =========================================================================

    /**
     * Validates if author ID exists in the system.
     * 
     * @param id        the author ID to validate
     * @param authorDAO author data access object
     * @return true if author exists, false otherwise
     */
    public static boolean isValidAuthorID(int id, AuthorDAO authorDAO) {
        return authorDAO.findById(id) != null;
    }

    /**
     * Validates if category ID exists in the system.
     * 
     * @param id          the category ID to validate
     * @param categoryDAO category data access object
     * @return true if category exists, false otherwise
     */
    public static boolean isValidCategoryID(int id, CategoryDAO categoryDAO) {
        return categoryDAO.findById(id) != null;
    }

    /**
     * Validates if user ID exists in the system.
     * 
     * @param id      the user ID to validate
     * @param userDAO user data access object
     * @return true if user exists, false otherwise
     */
    public static boolean isValidUserID(int id, UserDAO userDAO) {
        try {
            return userDAO.findById(id) != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Validates if loan ID exists in the system.
     * 
     * @param id      the loan ID to validate
     * @param loanDAO loan data access object
     * @return true if loan exists, false otherwise
     */
    public static boolean isValidLoanID(int id, LoanDAO loanDAO) {
        return loanDAO.findById(id) != null;
    }

    /**
     * Validates if password ID exists in the system.
     * 
     * @param id          the password ID to validate
     * @param passwordDAO password data access object
     * @return true if password exists, false otherwise
     */
    public static boolean isValidPasswordID(int id, PasswordDAO passwordDAO) {
        return passwordDAO.findById(id) != null;
    }

    /**
     * Validates if book ID exists in the system.
     * 
     * @param id      the book ID to validate
     * @param bookDAO book data access object
     * @return true if book exists, false otherwise
     */
    public static boolean isValidBookID(int id, BookDAO bookDAO) {
        return bookDAO.findById(id) != null;
    }

    /**
     * Validates if copy ID exists in the system.
     * 
     * @param id      the copy ID to validate
     * @param copyDAO copy data access object
     * @return true if copy exists, false otherwise
     */
    public static boolean isValidCopyID(int id, CopyDAO copyDAO) {
        return copyDAO.findById(id) != null;
    }

    // =========================================================================
    // GENERAL PURPOSE VALIDATION METHODS
    // =========================================================================

    /**
     * Validates if a date range is valid (start date before end date).
     * 
     * @param start the start date
     * @param end   the end date
     * @return true if date range is valid, false otherwise
     */
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
        return false;
    }
    
    if (startDate.isAfter(endDate)) {
        return false;
    }
    
    // AÑADE esta validación
    if (startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
        return false;
    }
    
    return true;
}

    /**
     * Validates email format using RFC 5322 standard.
     * 
     * @param email the email to validate
     * @return true if email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Validates Spanish phone number format.
     * 
     * @param phone the phone number to validate
     * @return true if phone number is valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone != null) {
            boolean validNumber = phone.startsWith("6") || phone.startsWith("7");
            boolean validLength = phone.length() == 9;
            return validNumber && validLength;
        }
        return false;
    }

    /**
     * Validates search term meets minimum length requirement.
     * 
     * @param searchTerm the search term to validate
     * @param minLength  the minimum required length
     * @throws ValidationException if search term is too short
     */
    public static void validateSearchTerm(String searchTerm, int minLength) throws ValidationException {
        if (!isValidString(searchTerm, minLength)) {
            throw new ValidationException("searchTerm", "INVALID_SEARCH_TERM",
                    "Search term must be at least " + minLength + " characters long");
        }
    }

    /**
     * Validates limit parameter for pagination and queries.
     * 
     * @param limit the limit to validate
     * @throws ValidationException if limit is not positive
     */
    public static void validateLimit(int limit) throws ValidationException {
        if (limit <= 0) {
            throw new ValidationException("limit", "INVALID_LIMIT",
                    "Limit must be greater than 0");
        }
    }

    // =========================================================================
    // STRING VALIDATION UTILITIES
    // =========================================================================

    /**
     * Validates if a string is not null and not empty after trimming.
     * 
     * @param value the string to validate
     * @return true if string is valid, false otherwise
     */
    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates if a string meets minimum length requirement.
     * 
     * @param value     the string to validate
     * @param minLength the minimum required length
     * @return true if string is valid, false otherwise
     */
    public static boolean isValidString(String value, int minLength) {
        return isValidString(value) && value.trim().length() >= minLength;
    }
}