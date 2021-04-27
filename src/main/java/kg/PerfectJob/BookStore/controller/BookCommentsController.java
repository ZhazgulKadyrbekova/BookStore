package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.CommentDTO;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/book/comment")
public class BookCommentsController {
    private final BookService bookService;

    public BookCommentsController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/{bookID}")
    public Book createComment(@PathVariable Long bookID, @RequestBody CommentDTO commentDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.createComment(bookID, commentDTO, principal.getName());
    }

    @PutMapping("/{bookID}/{commentID}")
    public Book updateComment(@PathVariable Long bookID, @RequestBody CommentDTO commentDTO,
                              @PathVariable Long commentID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.updateComment(bookID, commentDTO, commentID, principal.getName());
    }

    @DeleteMapping("/{bookID}/{commentID}")
    public Book deleteComment(@PathVariable Long bookID, @PathVariable Long commentID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.deleteComment(bookID, commentID, principal.getName());
    }

}
