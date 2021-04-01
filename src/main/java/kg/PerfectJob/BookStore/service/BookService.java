package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.BookDTO;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.BookComment;
import kg.PerfectJob.BookStore.entity.Category;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.BookRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookCommentService commentService;

    public BookService(BookRepository bookRepository, @Lazy AuthorService authorService, @Lazy CategoryService categoryService, @Lazy BookCommentService commentService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.commentService = commentService;
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public List<Book> getAllByDeleted(boolean deleted) {
        return bookRepository.findAllByDeleted(deleted);
    }

    public Book dtoToBook(Book book, BookDTO bookDTO) {

        book.setName(bookDTO.getName());
        book.setUrl(bookDTO.getUrl());
        if (bookDTO.getAuthorID() != 0) {
            Author author = authorService.getAuthorByID(bookDTO.getAuthorID());
            book.setAuthor(author);
            book.setType(author.getType());
        }
        if (bookDTO.getCategoryID() != 0)
            book.setCategory(categoryService.getCategoryByID(bookDTO.getCategoryID()));
        return book;
    }

    public Book create(BookDTO bookDTO) {
        Book book = dtoToBook(new Book(), bookDTO);
        return bookRepository.save(book);
    }

    public Book getBookByID(long bookID) {
        return bookRepository.findById(bookID)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + bookID + " has not found"));
    }

    public List<Book> getAllBooksByAuthor(Author author) {
        return bookRepository.findAllByAuthor(author);
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

    public Book update(long bookID, BookDTO bookDTO) {
        Book book = dtoToBook(this.getBookByID(bookID), bookDTO);
        return bookRepository.save(book);
    }

    public String delete(long bookID) {
        Book book = this.getBookByID(bookID);
        if (book.isDeleted()) {
            for (BookComment comment : book.getComments()) {
                commentService.delete(comment);
            }
            bookRepository.delete(book);
            return "Book with ID " + book.getID() + " has been completely deleted.";
        } else {
            book.setDeleted(true);
            bookRepository.delete(book);
            return "Book with ID " + book.getID() + " has been archived.";
        }
    }

    public Book unarchive(long bookID) {
        Book book = this.getBookByID(bookID);
        book.setDeleted(!book.isDeleted());
        return bookRepository.save(book);
    }
}
