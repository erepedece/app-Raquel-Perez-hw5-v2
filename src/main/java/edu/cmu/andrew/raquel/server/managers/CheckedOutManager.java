package edu.cmu.andrew.raquel.server.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import edu.cmu.andrew.raquel.server.exceptions.AppException;
import edu.cmu.andrew.raquel.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.raquel.server.models.CheckedOut;
import edu.cmu.andrew.raquel.server.utils.MongoPool;
import edu.cmu.andrew.raquel.server.models.Borrower;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.lang.String;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.excludeId;

public class CheckedOutManager extends Manager {
    public static CheckedOutManager _self;
    private MongoCollection<Document> checkedOutCollection;
    private MongoCollection<Document> bookCollection;
    private MongoCollection<Document> borrowerCollection;

    public CheckedOutManager() {
        this.checkedOutCollection = MongoPool.getInstance().getCollection("CheckedOutBooks");
        this.bookCollection = MongoPool.getInstance().getCollection("Books");
        this.borrowerCollection = MongoPool.getInstance().getCollection("Borrowers");
    }

    public static CheckedOutManager getInstance(){
        if (_self == null)
            _self = new CheckedOutManager();
        return _self;
    }

    // return book if bookId & borrowerId exist
    public String collectionCheckedOut(String book_id, String borrower_id) {

        Document myBorrower =
                borrowerCollection.find(eq("borrower_id", borrower_id)).projection(fields(include(
                        "borrower_name"),
                        excludeId())).first();

        // if borrowerid doesn't exist
        if (myBorrower == null) {
            return "Borrower with Id " + borrower_id + " does not exist";
        }

        Document myBook = bookCollection.find(eq("book_id", book_id)).projection(fields(include(
                "title", "available","author"),
                excludeId())).first();

        // if bookid doesn't exist
        if (myBook == null) {
            return "Book with Id " + book_id + " does not exist.";
        }

        // if bookid & borrowerid exist, update the corresponding info
        if (myBook.getString("available").equals("false")) {

            return "'" + myBook.getString("title") + "' is already checked out by " +
                    "someone.";
        }

        Document document =
                new Document("book_id", book_id).append("borrower_id", borrower_id).append("title",myBook.getString("title")).append("author",myBook.getString("author"))
                        .append("borrower_name",myBorrower.getString("borrower_name"));
        checkedOutCollection.insertOne(document);

        collectionUpdate("Books", book_id, "false");

        // return message confirmation
        return "'" + myBorrower.getString("borrower_name") + "' has checked out '" + myBook.getString("title") + "'";
    }

    private void collectionUpdate(String collectionName, String book_id,
                                         String available) {
        bookCollection.updateOne(Filters.eq("book_id", book_id), Updates.set("available",
                available));
    }


    // shows checkedout books
    public ArrayList<CheckedOut> getCheckedOutList() throws AppException {
        try{
            ArrayList<CheckedOut> checkedOutList = new ArrayList<>();
            FindIterable<Document> coDocs = checkedOutCollection.find();

            for(Document coDoc: coDocs) {
                CheckedOut checkedOut = new CheckedOut(
                        coDoc.getObjectId("_id").toString(),
                        coDoc.getString("book_id"),
                        coDoc.getString("borrower_id"),
                        coDoc.getString("title"),
                        coDoc.getString("author"),
                        coDoc.getString("borrower_name")
                );
                checkedOutList.add(checkedOut);
            }
            return new ArrayList<>(checkedOutList);
        } catch(Exception e){
            throw handleException("Get CheckOut List", e);
        }
    }


    // return book if bookId & borrowerId exist
    public String collectionReturn(String book_id, String borrower_id) {

        Document myBorrower = borrowerCollection.find(eq("borrower_id", borrower_id)).projection(fields(include(
                "borrower_name"),
                excludeId())).first();

        // if borrowerid doesn't exist
        if (myBorrower == null) {
            //System.out.println("Borrower with Id "+borrower_id +" does not exist");
            return "Borrower with Id " + borrower_id + " does not exist";
        }

        Document myBook = bookCollection.find(eq("book_id", book_id)).projection(fields(include(
                "title", "available"),
                excludeId())).first();

        // if bookid doesn't exist
        if (myBook == null) {
            //System.out.println("Book with Id "+ book_id + " does not exist.");
            return "Book with Id " + book_id + " does not exist.";
        }


        // confirm book has been checkedout by the corresponding person
        if (myBook.getString("available").equals("true")) {

            return "'" + myBorrower.getString("borrower_name") + "' has not currently checked out '" + myBook.getString("title") + "'.";
        }

        checkedOutCollection.deleteOne(Filters.eq("book_id", book_id));

        // return message confirmation
        collectionUpdate("Books", book_id, "true");
        return("'" + myBorrower.getString("borrower_name") + "' has returned '" + myBook.getString(
                "title") + "'.");
    }


}
