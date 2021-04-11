package kg.PerfectJob.BookStore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.PerfectJob.BookStore.dto.BookDTO;
import kg.PerfectJob.BookStore.entity.*;
import kg.PerfectJob.BookStore.exception.InvalidInputException;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.BookRepository;
import kg.PerfectJob.BookStore.repository.ImageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookCommentService commentService;
    private final ImageRepository imageRepository;

    public BookService(BookRepository bookRepository, @Lazy AuthorService authorService, @Lazy CategoryService categoryService, @Lazy BookCommentService commentService, ImageRepository imageRepository) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.imageRepository = imageRepository;
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


    public Book setImage(Long bookID, MultipartFile multipartFile) throws IOException {

        final String urlKey = "cloudinary://122578963631996:RKDo37y7ru4nnuLsBGQbwBUk65o@zhazgul/"; //в конце добавляем '/'
        Image image = new Image();
        File file;
        try{
            file = Files.createTempFile(System.currentTimeMillis() + "",
                    Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().length()-4)) // .jpg
                    .toFile();
            multipartFile.transferTo(file);

            Cloudinary cloudinary = new Cloudinary(urlKey);
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            image.setName((String) uploadResult.get("public_id"));
            image.setUrl((String) uploadResult.get("url"));
            image.setFormat((String) uploadResult.get("format"));
            imageRepository.save(image);

            Book book = this.getBookByID(bookID);
            book.setImage(image);
            return bookRepository.save(book);
        } catch (IOException e){
            throw new IOException("Unable to set image to book\n" + e.getMessage());
        }
    }


    public String deleteImage(Long bookID) {
        Book book = this.getBookByID(bookID);
        book.setImage(null);
        bookRepository.save(book);
        return "Image successfully deleted";
    }

    public Book setData(MultipartFile file, long bookID) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new InvalidInputException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Book book = this.getBookByID(bookID);
            book.setData(file.getBytes());
            return bookRepository.save(book);
        } catch (IOException e) {
            throw new InvalidInputException("Could not store file " + fileName + ". Please, try again!" + e.getMessage());
        }
    }

}
