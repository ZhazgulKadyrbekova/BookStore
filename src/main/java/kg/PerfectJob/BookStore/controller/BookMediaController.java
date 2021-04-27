package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.BookService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

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
        return bookService.setData(bookID, file, principal.getName());
    }

    @PutMapping("/image/{bookID}")
    public Book setImage(@PathVariable Long bookID, @RequestParam("file") MultipartFile file,
                         Principal principal) throws IOException {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return bookService.setImage(bookID, file, principal.getName());
    }

    @DeleteMapping("/image/{bookID}")
    public ResponseMessage deleteImage(@PathVariable Long bookID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return new ResponseMessage(bookService.deleteImage(bookID, principal.getName()));
    }
}
