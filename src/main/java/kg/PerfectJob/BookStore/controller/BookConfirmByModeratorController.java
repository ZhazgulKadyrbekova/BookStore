package kg.PerfectJob.BookStore.controller;

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
@RequestMapping("/book/confirm")
public class BookConfirmByModeratorController {
    private final BookService bookService;

    public BookConfirmByModeratorController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllUnconfirmedBooks(Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.getBooksByConfirmation(principal.getName());
    }

    @PutMapping("/{bookID}")
    public Book confirmBookByID(@PathVariable Long bookID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} confirmed book by id: {}", email, bookID);
        return bookService.confirmBookByID(bookID, email);
    }

    @DeleteMapping("/{bookID}")
    public ResponseMessage deleteBookByID(@PathVariable Long bookID, @RequestParam String description, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} rejected book by id: {}\n\treason: {}", email, bookID, description);
        return new ResponseMessage(bookService.deleteBookByID(bookID, description, email));
    }

}
