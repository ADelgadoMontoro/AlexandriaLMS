package com.alexandrialms.model;

import java.time.LocalDate;

public class Author {
    
    private int authorID;
    private String firstName;
    private String lastName;
    private String nationality;
    private LocalDate birthDate;

    public int getAuthorID() {
        return this.authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Author() {
    }

    public Author(String firstName, String lastName, String  nationality, LocalDate birthDate) {
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Author [authorID=" + authorID + ", firstName=" + firstName + ", lastName=" + lastName + ", nationality="
                + nationality + ", birthDate=" + birthDate + "]";
    }



    

    
}
