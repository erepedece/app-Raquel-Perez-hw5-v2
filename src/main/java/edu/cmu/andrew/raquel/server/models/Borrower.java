package edu.cmu.andrew.raquel.server.models;


public class Borrower {

    String id = null;
    String borrower_id = null;
    String borrower_name = null;
    String phone = null;

    public Borrower(String id, String borrower_id, String borrower_name, String phone) {
        this.id = id;
        this.borrower_id = borrower_id;
        this.borrower_name = borrower_name;
        this.phone = phone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getBorrower_id() {
        return borrower_id;
    }

    public String getBorrower_name() {
        return borrower_name;
    }

    public String getPhone() {
        return phone;
    }

}


