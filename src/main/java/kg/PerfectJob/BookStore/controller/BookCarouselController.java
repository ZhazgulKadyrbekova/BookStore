package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.exception.InvalidInputException;
import kg.PerfectJob.BookStore.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/carousel")
public class BookCarouselController {
    private final BookService bookService;

    public BookCarouselController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getTopTenBooksWithHighRatingByType(@RequestParam("type") String type) {
        if (!type.equalsIgnoreCase("NEW") && !type.equalsIgnoreCase("OLD"))
            throw new InvalidInputException("Be sure to send 'new' or 'old' types");
        return bookService.getTopTenBooksWithHighRatingByType(type);
    }


}
