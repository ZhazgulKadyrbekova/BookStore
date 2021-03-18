package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.BookDTO;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.BookComment;
import kg.PerfectJob.BookStore.service.BookCommentService;
import kg.PerfectJob.BookStore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookCommentService bookCommentService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    @GetMapping("/deleted/{deleted}")
    public List<Book> getAllBooksByDeleted(@PathVariable boolean deleted) {
        return bookService.getAllByDeleted(deleted);
    }

    @PostMapping
    public Book createNewBook(@RequestBody BookDTO bookDTO) {
        return bookService.create(bookDTO);
    }

    @GetMapping("/{id}")
    public Book getBookByID(@PathVariable Long id) {
        return bookService.getBookByID(id);
    }

    @GetMapping("/comment/{bookID}")
    public List<BookComment> getAllCommentsOfBook(@PathVariable Long bookID) {
        return bookCommentService.getAllByBook(bookID);
    }
}
