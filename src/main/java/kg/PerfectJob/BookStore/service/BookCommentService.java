package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.CommentDTO;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.BookComment;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.BookCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCommentService {
    private final BookCommentRepository bookCommentRepository;
    private final UserService userService;
    private final BookService bookService;

    public BookCommentService(BookCommentRepository bookCommentRepository, UserService userService, BookService bookService) {
        this.bookCommentRepository = bookCommentRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    public BookComment getByID(long commentID) {
        return bookCommentRepository.findById(commentID)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentID + " has not found"));
    }

    public BookComment create(CommentDTO commentDTO, String email) {
        BookComment comment = new BookComment();
        comment.setRating(commentDTO.getRating());
        comment.setDescription(commentDTO.getDescription());
        comment.setUser(userService.findUserByEmail(email));
        return bookCommentRepository.save(comment);
    }

    public BookComment update(BookComment comment, CommentDTO commentDTO, String email) {
        comment.setRating(commentDTO.getRating());
        comment.setDescription(commentDTO.getDescription());
        comment.setUser(userService.findUserByEmail(email));
        return bookCommentRepository.save(comment);
    }

    public void delete(BookComment comment) {
        bookCommentRepository.delete(comment);
    }
}
