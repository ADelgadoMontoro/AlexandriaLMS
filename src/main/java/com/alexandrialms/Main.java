package com.alexandrialms;

import com.alexandrialms.*;
import com.alexandrialms.dao.*;
import com.alexandrialms.dao.impl.AuthorDAO;
import com.alexandrialms.dao.impl.BookDAO;
import com.alexandrialms.model.*;
import com.alexandrialms.service.impl.AuthorServiceImpl;
import com.alexandrialms.util.DBConnection;
import com.alexandrialms.util.ValidationHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


// En tu main o tests
AuthorDAO authorDAO = new AuthorDAO();
AuthorServiceImpl authorService = new AuthorServiceImpl(authorDAO);

List<Author> authorsWithBooks = authorService.findAuthorsWithBooks();
System.out.println("Autores con libros: " + authorsWithBooks.size());

authorsWithBooks.forEach(author -> 
    System.out.println(author.getFirstName() + " " + author.getLastName())
);
    
}
}