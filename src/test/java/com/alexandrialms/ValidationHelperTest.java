package com.alexandrialms;
import static org.junit.Assert.*;

import org.junit.Before;

import java.time.LocalDate;
import java.util.Arrays;

import com.alexandrialms.dao.impl.CategoryDAO;
import com.alexandrialms.dao.impl.UserDAO;
import com.alexandrialms.model.Category;
import com.alexandrialms.model.User;
import com.alexandrialms.util.ValidationHelper;

public class ValidationHelperTest {

    private UserDAO mockUserDAO;
    private CategoryDAO mockCategoryDAO;

    @Before
    public void setUp() {
        mockUserDAO = new UserDAO() {
            @Override
            public java.util.List<User> findAll() {
                User u1 = new User(); u1.setUserID(1);
                User u2 = new User(); u2.setUserID(2);
                return Arrays.asList(u1, u2);
            }
        };
        mockCategoryDAO = new CategoryDAO() {
            @Override
            public java.util.List<Category> findAll() {
                Category c1 = new Category(); c1.setCategoryID(1);
                Category c2 = new Category(); c2.setCategoryID(2);
                return Arrays.asList(c1, c2);
            }
        };
    }    

        

    @org.junit.Test
    public void testValidISBN10() {
        assertTrue(ValidationHelper.isValidISBN("0-306-40615-2"));  // con guiones
        assertTrue(ValidationHelper.isValidISBN("0306406152"));     // sin guiones
        assertTrue(ValidationHelper.isValidISBN("0 306 40615 2"));  // con espacios
        assertTrue(ValidationHelper.isValidISBN("0-321-14653-0"));  // válido por la cara
    }

    @org.junit.Test
    public void testValidISBN10WithX() {
        assertTrue(ValidationHelper.isValidISBN("0-8044-2957-X")); // ISBN-10 con dígito de control X
        assertTrue(ValidationHelper.isValidISBN("080442957X")); // Lo mismo sin guiones
    }

    @org.junit.Test
    public void testInvalidISBN10() {
        assertFalse(ValidationHelper.isValidISBN("0-306-40615-3")); // dígito de control incorrecto
        assertFalse(ValidationHelper.isValidISBN("1234567890"));    // formato correcto pero control no
    }

    @org.junit.Test
    public void testValidISBN13() {
        assertTrue(ValidationHelper.isValidISBN("978-3-16-148410-0")); // con guiones
        assertTrue(ValidationHelper.isValidISBN("9783161484100"));     // sin guiones
        assertTrue(ValidationHelper.isValidISBN("978 0 306 40615 7")); // con espacios
    }

    @org.junit.Test
    public void testInvalidISBN13() {
        assertFalse(ValidationHelper.isValidISBN("978-3-16-148410-1")); // control incorrecto
        assertFalse(ValidationHelper.isValidISBN("9783161484101"));     // control incorrecto todo junto
    }

    @org.junit.Test
    public void testNullOrEmpty() {
        assertFalse(ValidationHelper.isValidISBN(null)); //isbn viene null
        assertFalse(ValidationHelper.isValidISBN("")); //cadena vacía
        assertFalse(ValidationHelper.isValidISBN(" ")); //un espacio
    }

    @org.junit.Test
    public void testCompletelyInvalid() {
        assertFalse(ValidationHelper.isValidISBN("abcdefg"));
        assertFalse(ValidationHelper.isValidISBN("123")); 
        assertFalse(ValidationHelper.isValidISBN("978-3-16-1484")); // demasiaod corto
    }

    @org.junit.Test

    public void testIsValidPhone() {
        assertTrue(ValidationHelper.isValidPhone("612345678"));
        assertTrue(ValidationHelper.isValidPhone("712345678"));
        assertFalse(ValidationHelper.isValidPhone("512345678"));
        assertFalse(ValidationHelper.isValidPhone("61234567")); 
        assertFalse(ValidationHelper.isValidPhone(null));
    }

    @org.junit.Test

    public void testIsValidEmail() {
        assertTrue(ValidationHelper.isValidEmail("user@example.com"));
        assertTrue(ValidationHelper.isValidEmail("user.name+tag@sub.domain.org"));
        assertFalse(ValidationHelper.isValidEmail("user@domain"));
        assertFalse(ValidationHelper.isValidEmail("user@@domain.com"));
        assertFalse(ValidationHelper.isValidEmail(null));
    }

    @org.junit.Test

    public void testIsValidDateRange() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(5);

        assertTrue(ValidationHelper.isValidDateRange(start, end));
        assertFalse(ValidationHelper.isValidDateRange(end, start)); // inicio > fin
        assertFalse(ValidationHelper.isValidDateRange(null, end));
        assertFalse(ValidationHelper.isValidDateRange(start, null));
    }

    @org.junit.Test

    public void testIsValidUserID() {
        assertTrue(ValidationHelper.isValidUserID(1, mockUserDAO));
        assertTrue(ValidationHelper.isValidUserID(2, mockUserDAO));
        assertFalse(ValidationHelper.isValidUserID(3, mockUserDAO));
    }

    @org.junit.Test

    public void testIsValidCategoryID() {
        assertTrue(ValidationHelper.isValidCategoryID(1, mockCategoryDAO));
        assertTrue(ValidationHelper.isValidCategoryID(2, mockCategoryDAO));
        assertFalse(ValidationHelper.isValidCategoryID(3, mockCategoryDAO));
    }
}


