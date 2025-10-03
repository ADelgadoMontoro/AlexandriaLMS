package com.alexandrialms.model;

public class Book {
    
    private int bookID;
    private String title;
    private String isbn;      
    private int pubYear;
    private int categoryId;

    
    public int getBookID() {
        return bookID;
    }



    public void setBookID(int bookID) {
        this.bookID = bookID;
    }



    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
    }



    public String getIsbn() {
        return isbn;
    }



    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }



    public int getPubYear() {
        return pubYear;
    }



    public void setPubYear(int pubYear) {
        this.pubYear = pubYear;
    }



    public int getCategoryId() {
        return categoryId;
    }



    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }



    public Book() {
    }



    public Book(String title, String isbn, int pubYear, int categoryId) {
        this.title = title;
        this.isbn = isbn;
        this.pubYear = pubYear;
        this.categoryId = categoryId;
    }



    @Override
    public String toString() {
        return "Book [bookID=" + bookID + ", title=" + title + ", isbn=" + isbn + ", pubYear=" + pubYear
                + ", categoryId=" + categoryId + "]";
    }


}

