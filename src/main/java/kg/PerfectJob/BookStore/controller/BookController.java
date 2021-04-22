package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.BookDTO;
import kg.PerfectJob.BookStore.dto.CommentDTO;
import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.BookService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/deleted/{deleted}")
    public List<Book> getAllBooksByDeleted(@PathVariable boolean deleted) {
        return bookService.getAllByDeleted(deleted);
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

    @PutMapping("/{bookID}/data")
    public Book uploadFile(@RequestBody MultipartFile file, @PathVariable Long bookID)
            throws IOException {
        return bookService.setData(bookID, file);
    }

    @PostMapping("/{bookID}/comment")
    public Book createComment(@PathVariable Long bookID, @RequestBody CommentDTO commentDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.createComment(bookID, commentDTO, principal.getName());
    }

    @PutMapping("/{bookID}/comment/{commentID}")
    public Book updateComment(@PathVariable Long bookID, @RequestBody CommentDTO commentDTO,
                              @PathVariable Long commentID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.updateComment(bookID, commentDTO, commentID, principal.getName());
    }

    @DeleteMapping("/{bookID}/comment/{commentID}")
    public Book deleteComment(@PathVariable Long bookID, @PathVariable Long commentID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.deleteComment(bookID, commentID, principal.getName());
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

    @PutMapping("/{bookID}/image")
    public Book setImage(@PathVariable Long bookID, @RequestParam("file") MultipartFile file)
            throws IOException {
        return bookService.setImage(bookID, file);
    }

    @DeleteMapping("/{bookID}/image")
    public ResponseMessage deleteImage(@PathVariable Long bookID) {
        return new ResponseMessage(bookService.deleteImage(bookID));
    }

    @GetMapping("/confirmed/{confirmed}")
    public List<Book> getBooksByConfirmation(@PathVariable Boolean confirmed, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.getBooksByConfirmation(confirmed, principal.getName());
    }

    @PutMapping("/confirm/{bookID}")
    public Book confirmBookByID(@PathVariable Long bookID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.confirmBookByID(bookID, principal.getName());
    }

    @DeleteMapping("/confirm/{bookID}")
    public ResponseMessage deleteBookByID(@PathVariable Long bookID, @RequestParam String description, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return new ResponseMessage(bookService.deleteBookByID(bookID, description, principal.getName()));
    }

}
