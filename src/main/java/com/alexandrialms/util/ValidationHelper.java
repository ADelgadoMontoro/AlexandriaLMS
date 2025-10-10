package com.alexandrialms.util;

import java.time.LocalDate;
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

public class ValidationHelper {

    // AUTHOR VALIDATION

    public static boolean isValidAuthorID(int id, AuthorDAO authorDAO) {
        return authorDAO.findById(id) != null;
    }

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

    public static boolean validateControlDigitISBN(String isbn) {
        isbn = cleanISBN(isbn);
        if (isbn.length() == 10)
            return validateISBN10(isbn);
        if (isbn.length() == 13)
            return validateISBN13(isbn);
        return false;
    }

    private static String cleanISBN(String isbn) {
        if (isbn == null)
            return "";
        return isbn.replaceAll("(?i)ISBN(?:-1[03])?:?", "")
                .replaceAll("[-\\s]", "");
    }

    public static boolean isValidCategoryID(int id, CategoryDAO categoryDAO) { // Inyección de dependencias?
        return categoryDAO.findAll()
                .stream()
                .anyMatch(c -> c.getCategoryID() == id);
    }

    public static boolean isValidUserID(int id, UserDAO userDAO) {
        return userDAO.findAll()
                .stream()
                .anyMatch(c -> c.getUserID() == id);
    }

    public static boolean isValidLoanID(int id, LoanDAO loanDAO) {
        return loanDAO.findAll()
                .stream()
                .anyMatch(c -> c.getLoanID() == id);
    }

    public static boolean isValidPasswordID(int id, PasswordDAO passwordDAO) {
        return passwordDAO.findAll()
                .stream()
                .anyMatch(c -> c.getPasswordID() == id);
    }

    public static boolean isValidBookID(int id, BookDAO bookDAO) {
        return bookDAO.findAll()
                .stream()
                .anyMatch(c -> c.getBookID() == id);
    }

    public static boolean isValidCopyID(int id, CopyDAO copyDAO) {
        return copyDAO.findAll()
                .stream()
                .anyMatch(c -> c.getCopyID() == id);
    }

    public static boolean isValidDateRange(LocalDate start, LocalDate end) {
        if (start != null && end != null && start.isBefore(end))
            return true;
        return false;
    }

    public static boolean isValidEmail(String email) {

        if (email == null)
            return false;
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone != null) {
            boolean validNumber = phone.startsWith("6") || phone.startsWith("7");
            boolean validLength = phone.length() == 9;
            return validNumber && validLength;
        }
        return false;

    }

    // Metahelper methods

    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidString(String value, int minLength) {
        return isValidString(value) && value.trim().length() >= minLength;
    }

}