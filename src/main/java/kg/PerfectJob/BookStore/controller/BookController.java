package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.BookDTO;
import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
        return bookService.create(bookDTO, principal.getName());
    }

    @GetMapping("/{id}")
    public Book getBookByID(@PathVariable Long id) {
        return bookService.getBookByID(id);
    }

    @PutMapping("/{id}")
    public Book updateBookInfo(@PathVariable("id") Long bookID, @RequestBody BookDTO bookDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.update(bookID, bookDTO, principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteBookByID(@PathVariable("id") Long bookID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return new ResponseMessage(bookService.delete(bookID, principal.getName()));
    }

}
