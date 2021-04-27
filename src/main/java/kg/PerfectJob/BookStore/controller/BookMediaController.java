package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.service.BookService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookMediaController {
    private final BookService bookService;

    public BookMediaController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/data/{bookID}")
    public Book uploadFile(@RequestBody MultipartFile file, @PathVariable Long bookID)
            throws IOException {
        return bookService.setData(bookID, file);
    }

    @PutMapping("/image/{bookID}")
    public Book setImage(@PathVariable Long bookID, @RequestParam("file") MultipartFile file)
            throws IOException {
        return bookService.setImage(bookID, file);
    }

    @DeleteMapping("/image/{bookID}")
    public ResponseMessage deleteImage(@PathVariable Long bookID) {
        return new ResponseMessage(bookService.deleteImage(bookID));
    }
}
