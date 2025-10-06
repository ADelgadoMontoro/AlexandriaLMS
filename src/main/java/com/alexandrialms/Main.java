package com.alexandrialms;

import com.alexandrialms.*;
import com.alexandrialms.dao.*;
import com.alexandrialms.model.*;
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


        List<Book> books = new ArrayList<Book>();

        BookDAO bookDAO = new BookDAO();
        books=bookDAO.findAll();
        for (Book book : books) {
            System.out.println(book.getTitle());
        }

    
}
}