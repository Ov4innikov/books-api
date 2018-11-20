package ru.booksapi.restcontrollers;

import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.booksapi.entities.Book;
import ru.booksapi.entities.Genre;
import ru.booksapi.exceptions.RestException;
import ru.booksapi.exceptions.ServiceExeption;
import ru.booksapi.interfaces.BookService;
import ru.booksapi.repostitories.BooksRepository;
import ru.booksapi.services.Response;

import javax.servlet.http.HttpServletResponse;

/**
 * Contoller for operation with entity book: GET/POST/PUT/DELETE
 *
 * @author Eugene Ovchinnikov
 */
@Controller
@RequestMapping("/book")
@Api(value="booksapi", description="Operations pertaining to books")
public class BooksRestController {

    private static final Logger logger = LoggerFactory.getLogger(BooksRestController.class);

    @Autowired
    private Response response;

    @Autowired
    private BookService bookService;

    @ApiOperation(value = "Get a list of all books", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully operations"),
            @ApiResponse(code = 400, message = "List is empty"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/getAllBooks", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> getAllBooks(HttpServletResponse responseHttp) {
        Map<Integer,Map<String,String>> resultMap = null;
        try{
            resultMap = bookService.getAllBooks();
            return response.successResponse(resultMap);
        } catch (ServiceExeption serviceExeption){
            logger.error("Service error", serviceExeption);
            responseHttp.setStatus(400);
            return response.errorResponse("List is empty");
        } catch (Exception e) {
            logger.error("Error", e);
            responseHttp.setStatus(500);
            return response.errorResponse("Error");
        }
    }

    @ApiOperation(value = "Get a book by id", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully operations"),
            @ApiResponse(code = 400, message = "Book not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/getBookById/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> getBooksById(@PathVariable Long id, HttpServletResponse responseHttp) {
        Map<Integer,Map<String,String>> resultMap = null;
        try{
            resultMap = bookService.getBookById(id);
            return response.successResponse(resultMap);
        } catch (ServiceExeption serviceExeption){
            logger.error("Service error", serviceExeption);
            responseHttp.setStatus(400);
            return response.errorResponse(serviceExeption.getMessage());
        } catch (Exception e) {
            logger.error("Error", e);
            responseHttp.setStatus(500);
            return response.errorResponse("Error");
        }
    }

    @ApiOperation(value = "Get books by author id", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully operations"),
            @ApiResponse(code = 400, message = "Book not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/getBooksByAuthorId/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> getBooksByAuthorId(@PathVariable Long id, HttpServletResponse responseHttp) {
        Map<Integer,Map<String,String>> resultMap = null;
        try{
            resultMap = bookService.getBooksByAuthorId(id);
            return response.successResponse(resultMap);
        } catch (ServiceExeption serviceExeption){
            logger.error("Service error", serviceExeption);
            responseHttp.setStatus(400);
            return response.errorResponse(serviceExeption.getMessage());
        } catch (Exception e) {
            logger.error("Error", e);
            responseHttp.setStatus(500);
            return response.errorResponse("Error");
        }
    }

    @ApiOperation(value = "Get books by genre id", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully operations"),
            @ApiResponse(code = 400, message = "Book not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/getBooksByGenreId/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> getBooksByGenreId(@PathVariable Long id, HttpServletResponse responseHttp) {
        Map<Integer,Map<String,String>> resultMap = null;
        try{
            resultMap = bookService.getBooksByGenreId(id);
            return response.successResponse(resultMap);
        } catch (ServiceExeption serviceExeption){
            logger.error("Service error", serviceExeption);
            responseHttp.setStatus(400);
            return response.errorResponse(serviceExeption.getMessage());
        } catch (Exception e) {
            logger.error("Error", e);
            responseHttp.setStatus(500);
            return response.errorResponse("Error");
        }
    }

    @RequestMapping(value = "/updateAuthorById", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> updateAuthorById(@RequestBody Map<String,String> newBook, HttpServletResponse responseHttp) {
        try{
            bookService.updateBookById(newBook);
            logger.debug("GetId=" + newBook.get("id") + "; Series=" + newBook.get("series"));
            return response.successResponse("OK");
        } catch (ServiceExeption serviceExeption){
            logger.error("Service error", serviceExeption);
            responseHttp.setStatus(400);
            return response.errorResponse(serviceExeption.getMessage());
        } catch (Exception e) {
            logger.error("Error", e);
            responseHttp.setStatus(500);
            return response.errorResponse("Error");
        }
    }

    //Method for adding a book
    @RequestMapping(value = "/createBook", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> createBook(HttpServletResponse responseHttp) {
        bookService.createBook();
        return response.successResponse("Book created");
    }
}
