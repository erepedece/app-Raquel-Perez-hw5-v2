package edu.cmu.andrew.raquel.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.raquel.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.raquel.server.http.responses.AppResponse;
import edu.cmu.andrew.raquel.server.managers.BookManager;
import edu.cmu.andrew.raquel.server.managers.BorrowerManager;
import edu.cmu.andrew.raquel.server.managers.CheckedOutManager;
import edu.cmu.andrew.raquel.server.models.Book;
import edu.cmu.andrew.raquel.server.models.Borrower;
import edu.cmu.andrew.raquel.server.models.CheckedOut;
import edu.cmu.andrew.raquel.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;


@Path("/checkouts")
public class CheckedOutHttpInterface extends HttpInterface {


    private ObjectWriter ow;
    //private MongoCollection<Document> borrowerCollection = null;

    public CheckedOutHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    //URL:http://localhost:8080/api/checkouts?bookId=5da120556aafa420dc583381&borrowerId
    // =5da11977ec7b0220a3db2b81

    // checkout
    // http://localhost:8080/api/checkouts?bookId=L180&borrowerId=L157
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse checkoutBooks(Object request, @QueryParam("bookId") String bookId,
                                     @QueryParam("borrowerId") String borrowerId) {

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            AppLogger.info("Got an API call");
            CheckedOutManager check = CheckedOutManager.getInstance();
            String val = check.collectionCheckedOut(bookId, borrowerId);

            if(val != null)
                return new AppResponse(val);
            else
                throw new HttpBadRequestException(0, "Problem with getting checked out list");
        }catch (Exception e){
            throw handleException("GET /checkouts/", e);
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getCheckedOutList(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<CheckedOut> checkedOut = CheckedOutManager.getInstance().getCheckedOutList();

            if(checkedOut != null)
                return new AppResponse(checkedOut);
            else
                throw new HttpBadRequestException(0, "Problem with getting check out list");
        }catch (Exception e){
            throw handleException("GET /checkout", e);
        }
    }



    @DELETE
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteBooks(@QueryParam("bookId") String bookId,
                                   @QueryParam("borrowerId") String borrowerId){

        try{
            String val = CheckedOutManager.getInstance().collectionReturn(bookId,borrowerId);
            return new AppResponse(val);
        }catch (Exception e){
            throw handleException("DELETE books/{bookId}", e);
        }

    }

}
