package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.CommentDTO;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Log4j2
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
        String email = principal.getName();
        log.debug("User {} added comment in book id: {},\n\t comment info: {}", email, bookID, commentDTO);
        return bookService.createComment(bookID, commentDTO, email);
    }

    @PutMapping("/{bookID}/{commentID}")
    public Book updateComment(@PathVariable Long bookID, @RequestBody CommentDTO commentDTO,
                              @PathVariable Long commentID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.debug("User {} updated comment in book id: {},\n\tcomment id: {}\n\t comment info: {}", email, bookID, commentID, commentDTO);
        return bookService.updateComment(bookID, commentDTO, commentID, email);
    }

    @DeleteMapping("/{bookID}/{commentID}")
    public Book deleteComment(@PathVariable Long bookID, @PathVariable Long commentID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.debug("User {} updated comment in book id: {},\n\tcomment id: {}", email, bookID, commentID);
        return bookService.deleteComment(bookID, commentID, principal.getName());
    }

}
