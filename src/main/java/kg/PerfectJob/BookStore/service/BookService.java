package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.BookDTO;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CategoryService categoryService;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public List<Book> getAllByDeleted(boolean deleted) {
        return bookRepository.findAllByDeleted(deleted);
    }

    public Book create(BookDTO bookDTO) {
        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setUrl(bookDTO.getUrl());
        book.setType(bookDTO.getType());
        if (bookDTO.getAuthorID() != 0)
            book.setAuthor(authorService.getAuthorByID(bookDTO.getAuthorID()));
        if (bookDTO.getCategoryID() != 0)
            book.setCategory(categoryService.getCategoryByID(bookDTO.getCategoryID()));
        return bookRepository.save(book);
    }

    public Book getBookByID(long bookID) {
        return bookRepository.findById(bookID)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + bookID + " has not found"));
    }
}
