package edu.cmu.andrew.raquel.server.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.raquel.server.exceptions.AppException;
import edu.cmu.andrew.raquel.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.raquel.server.utils.MongoPool;
import edu.cmu.andrew.raquel.server.models.Book;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.lang.String;
import java.util.ArrayList;

public class BookManager extends Manager {
    public static BookManager _self;
    private MongoCollection<Document> bookCollection;


    public BookManager() {
        this.bookCollection = MongoPool.getInstance().getCollection("Books");
    }

    public static BookManager getInstance(){
        if (_self == null)
            _self = new BookManager();
        return _self;
    }


    public void createBook(Book book) throws AppException {

        try{
            JSONObject json = new JSONObject(book);

            Document newDoc = new Document()
                    .append("book_id", book.getBook_id())
                    .append("title", book.getTitle())
                    .append("author",book.getAuthor())
                    .append("available",book.getAvailable());
            if (newDoc != null)
                bookCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new book");

        }catch(Exception e){
            throw handleException("Create Book", e);
        }

    }

    public void updateBook(Book book) throws AppException {
        try {

            Bson filter = new Document("_id", new ObjectId(book.getId()));
            Bson newValue = new Document()
                    .append("book_id", book.getBook_id())
                    .append("title", book.getTitle())
                    .append("author",book.getAuthor())
                    .append("available",book.getAvailable());

            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                bookCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update book details");

        } catch(Exception e) {
            throw handleException("Update Book", e);
        }
    }

    public void deleteBook(String bookId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(bookId));
            bookCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Book", e);
        }
    }

    public ArrayList<Book> getBookList() throws AppException {
        try{
            ArrayList<Book> bookList = new ArrayList<>();
            FindIterable<Document> bookDocs = bookCollection.find();
            for(Document bookDoc: bookDocs) {
                Book book = new Book(
                        bookDoc.getObjectId("_id").toString(),
                        bookDoc.getString("book_id"),
                        bookDoc.getString("title"),
                        bookDoc.getString("author"),
                        bookDoc.getString("available")
                );
                bookList.add(book);
            }
            return new ArrayList<>(bookList);
        } catch(Exception e){
            throw handleException("Get Book List", e);
        }
    }

    public ArrayList<Book> getBookById(String id) throws AppException {
        try{
            ArrayList<Book> bookList = new ArrayList<>();
            FindIterable<Document> bookDocs = bookCollection.find();
            for(Document bookDoc: bookDocs) {
                if(bookDoc.getObjectId("_id").toString().equals(id)) {
                    Book book = new Book(
                            bookDoc.getObjectId("_id").toString(),
                            bookDoc.getString("book_id"),
                            bookDoc.getString("title"),
                            bookDoc.getString("author"),
                            bookDoc.getString("available")
                    );
                    bookList.add(book);
                }
            }
            return new ArrayList<>(bookList);
        } catch(Exception e){
            throw handleException("Get Book List", e);
        }
    }


}
