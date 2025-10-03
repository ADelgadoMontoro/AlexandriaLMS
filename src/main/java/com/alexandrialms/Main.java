package com.alexandrialms;

import com.alexandrialms.*;
import com.alexandrialms.dao.*;
import com.alexandrialms.model.*;
import com.alexandrialms.util.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


 UserDAO userDAO = new UserDAO();       

        List<User> users = userDAO.findAll();
        System.out.println("All users after insert:");
        for (User u : users) {
            System.out.println(u.getUserID() + " - " + u.getFirstName() + " " + u.getLastName() +
                    ", Email: " + u.getEmail() +
                    ", Role: " + u.getRole() +
                    ", Active: " + u.isActive());
        }

    
}
}