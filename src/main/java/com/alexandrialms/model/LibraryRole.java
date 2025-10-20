package com.alexandrialms.model;

public enum LibraryRole {
    READER(5),
    LIBRARIAN(10),
    ADMIN(20);
    
    private final int loanLimit;
    
    LibraryRole(int loanLimit) {
        this.loanLimit = loanLimit;
    }
    
    public int getLoanLimit() {
        return loanLimit;
    }
}
