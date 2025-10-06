package com.alexandrialms.model;

public class Copy {
    private int copyID;
    private int book_id;
    private String internal_code;
    private CopyStatus status;
    
    public int getCopyID() {
        return copyID;
    }
    public void setCopyID(int copyID) {
        this.copyID = copyID;
    }
    public int getBook_id() {
        return book_id;
    }
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }
    public String getInternal_code() {
        return internal_code;
    }
    public void setInternal_code(String internal_code) {
        this.internal_code = internal_code;
    }
    public CopyStatus getStatus() {
        return status;
    }
    public void setStatus(CopyStatus status) {
        this.status = status;
    }
    public Copy() {
    }
    public Copy(int book_id, String internal_code, CopyStatus status) {
        this.book_id = book_id;
        this.internal_code = internal_code;
        this.status = status;
    }
    
}
