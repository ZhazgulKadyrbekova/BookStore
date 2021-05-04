package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookMediaController {
    private final BookService bookService;

    public BookMediaController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/data/{bookID}")
    public Book uploadFile(@RequestBody MultipartFile file, @PathVariable Long bookID, Principal principal)
            throws IOException {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} set data to book id: {}", email, bookID);
        return bookService.setData(bookID, file, email);
    }

    @PutMapping("/image/{bookID}")
    public Book setImage(@PathVariable Long bookID, @RequestParam("file") MultipartFile file,
                         Principal principal) throws IOException {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} set image to book id: {}", email, bookID);
        return bookService.setImage(bookID, file, principal.getName());
    }

    @DeleteMapping("/image/{bookID}")
    public ResponseMessage deleteImage(@PathVariable Long bookID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} delete image of book id: {}", email, bookID);
        return new ResponseMessage(bookService.deleteImage(bookID, principal.getName()));
    }
}
