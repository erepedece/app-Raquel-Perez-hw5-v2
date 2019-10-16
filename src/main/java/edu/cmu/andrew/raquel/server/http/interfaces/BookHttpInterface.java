package edu.cmu.andrew.raquel.server.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.raquel.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.raquel.server.http.responses.AppResponse;
import edu.cmu.andrew.raquel.server.http.utils.PATCH;
import edu.cmu.andrew.raquel.server.managers.BookManager;
import edu.cmu.andrew.raquel.server.managers.BorrowerManager;
import edu.cmu.andrew.raquel.server.models.Book;
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

@Path("/books")

public class BookHttpInterface extends HttpInterface{

    private ObjectWriter ow;

    public BookHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    // http://localhost:8080/api/books
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postBooks(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Book newbook = new Book(
                    null,
                    json.getString("book_id"),
                    json.getString("title"),
                    json.getString("author"),
                    json.getString("available")
            );
            BookManager.getInstance().createBook(newbook);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST books", e);
        }
    }

    // http://localhost:8080/api/books
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getBooks(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Book> books = BookManager.getInstance().getBookList();

            if(books != null)
                return new AppResponse(books);
            else
                throw new HttpBadRequestException(0, "Problem with getting books");
        }catch (Exception e){
            throw handleException("GET /books", e);
        }
    }


    @GET
    @Path("/{bookId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleBook(@Context HttpHeaders headers,
                                       @PathParam("bookId") String bookId){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Book> books = BookManager.getInstance().getBookById(bookId);

            if(books != null)
                return new AppResponse(books);
            else
                throw new HttpBadRequestException(0, "Problem with getting books");
        }catch (Exception e){
            throw handleException("GET /books/{bookId}", e);
        }
    }


    //available

    // http://localhost:8080/api/books/booksdetails?available=true

    @GET
    @Path("/booksdetails")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getAvailableBooks(@Context HttpHeaders headers,
                            @QueryParam("available") String available){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Book> books = BookManager.getInstance().getBookList();
            ArrayList<Book> availBook = new ArrayList<>();
            for(Book book1:books)
            {
                if(book1.getAvailable().equals("true"))
                {
                    availBook.add(book1);
                }
            }
            if(availBook!= null)
                return new AppResponse(availBook);
            else
                throw new HttpBadRequestException(0, "Problem with getting books");
        }catch (Exception e){
            throw handleException("GET /books?available=true", e);
        }
    }


    // books/{bookId} update
    //http://localhost:8080/api/books/5da502a2ea621531fef4d812
    @PATCH
    @Path("/{bookId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchBooks(Object request, @PathParam("bookId") String bookId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Book book = new Book(bookId,
                    json.getString("book_id"),
                    json.getString("title"),
                    json.getString("author"),
                    json.getString("available")
            );

            BookManager.getInstance().updateBook(book);

        }catch (Exception e){
            throw handleException("PATCH books/{bookId}", e);
        }

        return new AppResponse("Update Successful");
    }

    // http://localhost:8080/api/books/5da11982ec7b0220a3db2b82
    @DELETE
    @Path("/{bookId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteBooks(@PathParam("bookId") String bookId){

        try{
            BookManager.getInstance().deleteBook( bookId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE books/{bookId}", e);
        }
    }
}
