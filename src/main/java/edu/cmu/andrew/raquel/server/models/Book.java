package edu.cmu.andrew.raquel.server.models;

public class Book {

    String id = null;
    String book_id = null;
    String title = null;
    String author = null;
    String available = null;

    public Book(String id, String book_id, String title, String author, String available) {
        this.id = id;
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvailable() {
        return available;
    }


}


