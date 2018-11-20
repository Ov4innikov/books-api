package ru.booksapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.booksapi.entities.Author;
import ru.booksapi.entities.Book;
import ru.booksapi.entities.Genre;
import ru.booksapi.exceptions.ServiceExeption;
import ru.booksapi.interfaces.BookService;
import ru.booksapi.repostitories.AuthorsRepository;
import ru.booksapi.repostitories.BooksRepository;
import ru.booksapi.repostitories.GenresRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service of books books
 *
 * @author Eugene Ovchinnikov
 */
@Service
public class BookServiceImpl implements BookService{

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private AuthorsRepository authorsRepository;

    @Autowired
    private GenresRepository genresRepository;

    @Override
    public Map<Integer,Map<String,String>> getBookById(Long id) throws ServiceExeption {
        Map<Integer,Map<String,String>> resultMap = new HashMap<Integer,Map<String,String>>();
        Book book = null;
        if(booksRepository.findById(id).isPresent()){
            book = booksRepository.findById(id).get();
            resultMap.put(1, convertBookToMap(book));
        }else {
            throw new ServiceExeption("Book not found");
        }
        logger.debug("RESULT BOOK: " + book.getName());

        return resultMap;
    }

    @Override
    public Map<Integer,Map<String,String>> getBooksByAuthorId(Long authorId) throws ServiceExeption {
        Map<Integer,Map<String,String>> resultMap = new HashMap<Integer,Map<String,String>>();
        if(booksRepository.findAll().iterator().hasNext()==false) throw new ServiceExeption("Empty books map");
        int i = 1;
        for(Book item:booksRepository.findAll()) {
            if(item.getAuthor().getId() == authorId)resultMap.put(i++, convertBookToMap(item));
        }
        logger.debug("RESULT MAP" + resultMap.toString());
        return resultMap;
    }

    @Override
    public Map<Integer,Map<String,String>> getBooksByGenreId(Long genreId) throws ServiceExeption {
        Map<Integer,Map<String,String>> resultMap = new HashMap<Integer,Map<String,String>>();
        if(booksRepository.findAll().iterator().hasNext()==false) throw new ServiceExeption("Empty books map");
        int i = 1;
        for(Book item:booksRepository.findAll()) {
            if(item.getGenre().getId() == genreId)resultMap.put(i++, convertBookToMap(item));
        }
        logger.debug("RESULT MAP" + resultMap.toString());
        return resultMap;
    }

    @Override
    public Map<Integer,Map<String,String>> getAllBooks() throws ServiceExeption {
        logger.debug("Start method getAllBooks.");
        Map<Integer,Map<String,String>> resultMap = new HashMap<Integer,Map<String,String>>();
        int i = 1;
        for(Book item:booksRepository.findAll()) {
            resultMap.put(i++, convertBookToMap(item));
            logger.debug("Book id is " + item.getId());
        }
        logger.debug("RESULT MAP" + resultMap.toString());
        if (resultMap.size() == 0) throw new ServiceExeption("Empty books map");
        return resultMap;
    }

    @Override
    public void updateBookById(Map<String,String> newBook) throws ServiceExeption {
        Book book = null;
        logger.debug("updateBookById, id = " + Long.valueOf(newBook.get("id")) + "!!!");
        if(booksRepository.findById(Long.valueOf(newBook.get("id"))).isPresent()) {
            book = booksRepository.findById(Long.valueOf(newBook.get("id"))).get();
        }else {
            throw new ServiceExeption("Book not found");
        }
        Genre genre = genresRepository.findById(Long.valueOf(newBook.get("genreId"))).get();
        Author author = authorsRepository.findById(Long.valueOf(newBook.get("authorId"))).get();
        book.setSeries(newBook.get("series"));
        book.setName(newBook.get("name"));
        book.setCountOfPage(Integer.valueOf(newBook.get("countOfPage")));
        book.setDescription(newBook.get("description"));
        book.setGenre(genre);
        book.setAuthor(author);
        booksRepository.save(book);
    }

    //Method for adding a book
    public void createBook(){
        Author author = new Author();
        author.setLastName("Author last name");
        author.setFirstName("Author first name");
        author.setDateOfBirthDay(LocalDate.of(1900, 1,1));

        Genre genre = new Genre();
        genre.setGenre("Genre");

        Book book = new Book();
        book.setSeries("series");
        book.setName("Book name");
        book.setDescription("Description");
        book.setCountOfPage(100);
        book.setAuthor(author);
        book.setGenre(genre);

        booksRepository.save(book);
    }

    private Map<String,String> convertBookToMap(Book book){
        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put("name",book.getName());
        resultMap.put("description",book.getDescription());
        resultMap.put("series",book.getSeries());
        resultMap.put("authorFirstName",book.getAuthor().getFirstName());
        resultMap.put("genre",book.getGenre().getGenre());
        return resultMap;
    }
}