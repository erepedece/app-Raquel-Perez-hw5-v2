package edu.cmu.andrew.raquel.server.models;


public class CheckedOut {

    String id = null;
    String book_id = null;
    String borrower_id = null;
    String title = null;
    String author = null;
    String borrower_name = null;


    public CheckedOut(String id, String book_id, String borrower_id, String title, String author,
                      String borrower_name) {
        this.id = id;
        this.book_id = book_id;
        this.borrower_id = borrower_id;
        this.title = title;
        this.author = author;
        this.borrower_name = borrower_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBorrower_id() {
        return borrower_id;
    }

    public void setBorrower_id(String borrower_id) {
        this.borrower_id = borrower_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBorrower_name() {
        return borrower_name;
    }

    public void setBorrower_name(String borrower_name) {
        this.borrower_name = borrower_name;
    }
}


