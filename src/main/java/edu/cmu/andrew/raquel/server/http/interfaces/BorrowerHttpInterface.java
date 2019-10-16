package edu.cmu.andrew.raquel.server.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.raquel.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.raquel.server.http.responses.AppResponse;
import edu.cmu.andrew.raquel.server.http.utils.PATCH;
import edu.cmu.andrew.raquel.server.managers.BorrowerManager;
import edu.cmu.andrew.raquel.server.models.Borrower;
import edu.cmu.andrew.raquel.server.utils.*;
import edu.cmu.andrew.raquel.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/borrowers")

public class BorrowerHttpInterface extends HttpInterface{

    private ObjectWriter ow;

    public BorrowerHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    // http://localhost:8080/api/borrowers
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postBorrowers(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Borrower newborrower = new Borrower(
                    null,
                    json.getString("borrower_id"),
                    json.getString("borrower_name"),
                    json.getString("phone")
            );
            BorrowerManager.getInstance().createBorrower(newborrower);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST borrowers", e);
        }
    }

    // http://localhost:8080/api/borrowers
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getBorrowers(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Borrower> borrowers = BorrowerManager.getInstance().getBorrowerList();

            if(borrowers != null)
                return new AppResponse(borrowers);
            else
                throw new HttpBadRequestException(0, "Problem with getting borrowers");
        }catch (Exception e){
            throw handleException("GET /borrowers", e);
        }
    }

    // http://localhost:8080/api/borrowers
    @GET
    @Path("/{borrowerId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleBorrower(@Context HttpHeaders headers,
                                     @PathParam("borrowerId") String borrowerId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Borrower> borrowers =
                    BorrowerManager.getInstance().getBorrowerById(borrowerId);

            if(borrowers != null)
                return new AppResponse(borrowers);
            else
                throw new HttpBadRequestException(0, "Problem with getting borrowers");
        }catch (Exception e){
            throw handleException("GET /borrowers/{borrowerId}", e);
        }
    }

    // borrower/{borrowerId} update
    // http://localhost:8080/api/borrowers/5da11982ec7b0220a3db2b82
    @PATCH
    @Path("/{borrowerId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchBooks(Object request, @PathParam("borrowerId") String borrowerId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Borrower borrower = new Borrower(borrowerId,
                    json.getString("borrower_id"),
                    json.getString("borrower_name"),
                    json.getString("phone")
            );

            BorrowerManager.getInstance().updateBorrower(borrower);

        }catch (Exception e){
            throw handleException("PATCH borrowers/{borrowerId}", e);
        }

        return new AppResponse("Update Successful");
    }


    // http://localhost:8080/api/borrowers/5da11982ec7b0220a3db2b82
    @DELETE
    @Path("/{borrowerId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteBorrowers(@PathParam("borrowerId") String borrowerId){

        try{
            BorrowerManager.getInstance().deleteBorrower(borrowerId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE borrowers/{borrowerId}", e);
        }

    }


}
