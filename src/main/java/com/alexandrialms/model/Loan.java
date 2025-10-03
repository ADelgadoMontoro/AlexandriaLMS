package com.alexandrialms.model;

import java.time.LocalDate;

public class Loan {
    private int loanID;
    private int copyID;      
    private int userID;       
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean returned;
    
    public int getLoanID() {
        return loanID;
    }
    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }
    public LocalDate getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    public boolean isReturned() {
        return returned;
    }
    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public Loan(int loanID, int copyID, int userID, LocalDate loanDate, LocalDate returnDate, boolean returned) {
        this.loanID = loanID;
        this.copyID = copyID;
        this.userID = userID;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }
    public Loan() {
    }
    public int getCopyID() {
        return copyID;
    }
    public void setCopyID(int copyID) {
        this.copyID = copyID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    @Override
    public String toString() {
        return "Loan [loanID=" + loanID + ", copyID=" + copyID + ", userID=" + userID + ", loanDate=" + loanDate
                + ", returnDate=" + returnDate + ", returned=" + returned + "]";
    } 
    
    
}
