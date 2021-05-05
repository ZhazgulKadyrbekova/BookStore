package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.BookDTO;
import kg.PerfectJob.BookStore.dto.CommentDTO;
import kg.PerfectJob.BookStore.entity.*;
import kg.PerfectJob.BookStore.exception.AccessDeniedException;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.BookRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookCommentService commentService;
    private final MailService mailService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public BookService(BookRepository bookRepository, @Lazy AuthorService authorService,
                       @Lazy CategoryService categoryService, @Lazy BookCommentService commentService,
                       MailService mailService, UserService userService, CloudinaryService cloudinaryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.mailService = mailService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book dtoToBook(Book book, BookDTO bookDTO) {
        book.setName(bookDTO.getName());
        if (bookDTO.getCategoryID() != 0)
            book.setCategory(categoryService.getCategoryByID(bookDTO.getCategoryID()));
        return book;
    }

    public Book create(BookDTO bookDTO, String email) {
        User admin = userService.findUserByEmail(email);
        Author author = authorService.getAuthorByID(bookDTO.getAuthorID());
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            Book book = dtoToBook(new Book(), bookDTO);
            book.setAuthor(author);
            book.setType(author.getType());
            return bookRepository.save(book);
        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public Book getBookByID(long bookID) {
        return bookRepository.findById(bookID)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + bookID + " has not found"));
    }

    public List<Book> getAllBooksByAuthor(Author author) {
        return bookRepository.findAllByAuthorOrderByAverageRatingDesc(author);
    }

    public List<Book> getAllBooksByCategory(Category category) {
        return bookRepository.findAllByCategory(category);
    }

    public void setAuthorNull(Book book) {
        book.setAuthor(null);
        bookRepository.save(book);
    }

    public void setCategoryNull(Book book) {
        book.setCategory(null);
        bookRepository.save(book);
    }

    public Book update(long bookID, BookDTO bookDTO, String email) {
        User admin = userService.findUserByEmail(email);
        Author author = authorService.getAuthorByID(bookDTO.getAuthorID());
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            Book book = dtoToBook(this.getBookByID(bookID), bookDTO);
            book.setAuthor(author);
            book.setType(author.getType());
            return bookRepository.save(book);
        } else {
            throw new AccessDeniedException("Access Denied!");
        }


    }

    public String delete(long bookID, String email) {
        User admin = userService.findUserByEmail(email);
        Book book = this.getBookByID(bookID);
        Author author = book.getAuthor();
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            for (BookComment comment : book.getComments()) {
                commentService.delete(comment);
            }
            bookRepository.delete(book);
            return "Book with ID " + book.getID() + " has been completely deleted.";
        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public Book setImage(Long bookID, MultipartFile multipartFile, String email) throws IOException {
        User admin = userService.findUserByEmail(email);
        Book book = this.getBookByID(bookID);
        Author author = book.getAuthor();
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            Media image = cloudinaryService.createMediaFromMultipartFile(multipartFile);
            book.setImage(image);
            return bookRepository.save(book);
        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public String deleteImage(Long bookID, String email) {
        User admin = userService.findUserByEmail(email);
        Book book = this.getBookByID(bookID);
        Author author = book.getAuthor();
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            book.setImage(null);
            bookRepository.save(book);
            return "Image successfully deleted";

        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public Book setData(Long bookID, MultipartFile multipartFile, String email) throws IOException {
        User admin = userService.findUserByEmail(email);
        Book book = this.getBookByID(bookID);
        Author author = book.getAuthor();
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            Media data = cloudinaryService.createMediaFromMultipartFile(multipartFile);
            book.setData(data);
            return bookRepository.save(book);
        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public List<Book> getBooksByConfirmation(String email) {
        User admin = userService.findUserByEmail(email);
        if (admin.getRole().getName().equals("ROLE_ADMIN") || admin.getRole().getName().equals("ROLE_MODERATOR")) {
            return bookRepository.findAllByConfirmed(false);
        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public Book confirmBookByID(long bookID, String email) {
        User admin = userService.findUserByEmail(email);
        Book book = getBookByID(bookID);
        if (admin.getRole().getName().equals("ROLE_ADMIN") || admin.getRole().getName().equals("ROLE_MODERATOR")) {
            if (book.isConfirmed())
                throw new ResourceNotFoundException("Book with ID " + bookID + " is already confirmed");
            book.setConfirmed(true);
            return bookRepository.save(book);
        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public String deleteBookByID(long bookID, String description, String email) {
        User admin = userService.findUserByEmail(email);
        Book book = getBookByID(bookID);
        if (admin.getRole().getName().equals("ROLE_ADMIN") || admin.getRole().getName().equals("ROLE_MODERATOR")) {
            if (book.isConfirmed())
                throw new ResourceNotFoundException("Book with ID " + bookID + " is already confirmed");
            bookRepository.delete(book);
            String userEmail = book.getAuthor().getUser().getEmail();
            String text = "Your request was rejected\nDescription: " + description;
            if (mailService.send(userEmail, "Rejection of book uploading", text))
                return "Book with ID " + bookID + " has been deleted. User " + userEmail
                        + " will receive mail with description of rejection";
            else
                return "Book with ID " + bookID + " has been deleted. " +
                        "Errors with sending mail with description to user " + userEmail;
        } else {
            throw new AccessDeniedException("Access Denied!");
        }

    }

    public Book createComment(long bookID, CommentDTO commentDTO, String email) {
        Book book = getBookByID(bookID);
        List<BookComment> comments = book.getComments();
        BookComment comment = commentService.create(commentDTO, email);
        comments.add(comment);
        book.setComments(comments);
        double aveBookRating = (book.getAverageRating() + comment.getRating()) / 2;
        book.setAverageRating(aveBookRating);
        book = bookRepository.save(book);
        authorService.updateRating(book);
        return book;
    }

    public Book updateComment(long bookID, CommentDTO commentDTO, long commentID, String email) {
        Book book = getBookByID(bookID);
        List<BookComment> comments = book.getComments();
        BookComment comment = commentService.getByID(commentID);
        if (!comments.contains(comment))
            throw new ResourceNotFoundException("Comment with ID " + commentID + " has not found in list of this book.");
        if (!comment.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You can not edit someone's comments");
        }
        comments.remove(comment);
        double oldCommentRating = comment.getRating(), newCommentRating = commentDTO.getRating();
        comments.add(commentService.update(comment, commentDTO, email));
        book.setComments(comments);
        double aveRating = book.getAverageRating() - (oldCommentRating/2 - newCommentRating/2);
        book.setAverageRating(aveRating);
        book = bookRepository.save(book);
        authorService.updateRating(book);
        return book;
    }

    public Book deleteComment(long bookID, long commentID, String email) {
        Book book = getBookByID(bookID);
        List<BookComment> comments = book.getComments();
        BookComment comment = commentService.getByID(commentID);
        if (comment.getUser().getEmail().equals(email) || userService.findUserByEmail(email).getRole().getName().equals("ROLE_ADMIN")) {
            if (!comments.contains(comment))
                throw new ResourceNotFoundException("Comment with ID " + commentID + " has not found in list of this book.");
            comments.remove(comment);
            commentService.delete(comment);
            double aveRating = book.getAverageRating() - comment.getRating() / 2;
            book.setAverageRating(aveRating);
            book = bookRepository.save(book);
            authorService.updateRating(book);
            return book;
        } else {
            throw new AccessDeniedException("You can not delete someone's comments.");
        }
    }

    public List<Book> getTopTenBooksWithHighRatingByType(String type) {
        return bookRepository.findTop10ByTypeIgnoringCaseOrderByAverageRatingDesc(type);
    }

    public List<Book> getAllBooksOfAuthor(long authorID) {
        Author author = authorService.getAuthorByID(authorID);
        return bookRepository.findAllByAuthorOrderByAverageRatingDesc(author);
    }

}
