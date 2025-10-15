package com.alexandrialms;


import com.alexandrialms.dao.impl.AuthorDAO;
import com.alexandrialms.model.*;
import com.alexandrialms.service.impl.AuthorServiceImpl;
import java.util.List;

public class Main {
    public static void main(String[] args) {


AuthorDAO authorDAO = new AuthorDAO();
AuthorServiceImpl authorService = new AuthorServiceImpl(authorDAO);

List<Author> authorsWithBooks = authorService.findAuthorsWithBooks();
System.out.println("Autores con libros: " + authorsWithBooks.size());

authorsWithBooks.forEach(author -> 
    System.out.println(author.getFirstName() + " " + author.getLastName())
);
    
}
}