package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.BookComment;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.BookCommentRepository;
import kg.PerfectJob.BookStore.repository.BookRepository;
import kg.PerfectJob.BookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCommentService {
    @Autowired
    private BookCommentRepository bookCommentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    public List<BookComment> getAllByBook(long bookID) {
        Book book = bookService.getBookByID(bookID);
        return bookCommentRepository.findAllByBook(book);
    }

    public List<BookComment> getAllByDeleted(boolean deleted) {
        return bookCommentRepository.findAllByDeleted(deleted);
    }
}
