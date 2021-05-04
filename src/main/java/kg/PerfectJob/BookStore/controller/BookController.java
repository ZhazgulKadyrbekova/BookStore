package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.BookDTO;
import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    @PostMapping
    public Book createNewBook(@RequestBody BookDTO bookDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} created new book info: {}", email, bookDTO);
        return bookService.create(bookDTO, email);
    }

    @GetMapping("/{id}")
    public Book getBookByID(@PathVariable Long id) {
        return bookService.getBookByID(id);
    }

    @PutMapping("/{id}")
    public Book updateBookInfo(@PathVariable("id") Long bookID, @RequestBody BookDTO bookDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} updated book id: {}\n\tinfo: {}", email, bookID, bookDTO);
        return bookService.update(bookID, bookDTO, email);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteBookByID(@PathVariable("id") Long bookID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} deleted book id: {}", email, bookID);
        return new ResponseMessage(bookService.delete(bookID, email));
    }

    @GetMapping("/author/{id}")
    public List<Book> getAllBooksOfAuthor(@PathVariable("id") Long authorID) {
        return bookService.getAllBooksOfAuthor(authorID);
    }
}
