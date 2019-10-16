package edu.cmu.andrew.raquel.server.managers;

import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.raquel.server.exceptions.AppException;
import edu.cmu.andrew.raquel.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.raquel.server.utils.MongoPool;
import edu.cmu.andrew.raquel.server.utils.AppLogger;
import org.bson.Document;

public class Manager {
    protected MongoCollection<Document> bookCollection;
    protected MongoCollection<Document> borrowerCollection;
    protected MongoCollection<Document> checkedOutCollection;

    public Manager() {
        this.checkedOutCollection = MongoPool.getInstance().getCollection("CheckedOutBooks");
        this.bookCollection = MongoPool.getInstance().getCollection("Books");
        this.borrowerCollection = MongoPool.getInstance().getCollection("Borrowers");


    }

    protected AppException handleException(String message, Exception e){
        if (e instanceof AppException && !(e instanceof AppInternalServerException))
            return (AppException)e;
        AppLogger.error(message, e);
        return new AppInternalServerException(-1);
    }
}
