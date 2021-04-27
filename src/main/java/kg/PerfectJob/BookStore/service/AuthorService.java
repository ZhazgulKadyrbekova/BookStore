package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.AuthorDTO;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.Media;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.AccessDeniedException;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.repository.AuthorRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public AuthorService(AuthorRepository authorRepository, BookService bookService,
                         @Lazy UserService userService, CloudinaryService cloudinaryService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    private Author dtoToAuthor(Author author, AuthorDTO authorDTO) {
        author.setName(authorDTO.getName());
        author.setType(authorDTO.getType());
        author.setBirthDate(authorDTO.getBirthDate());
        author.setBiography(authorDTO.getBiography());

        return author;
    }

    public Author create(AuthorDTO authorDTO, String email) {
        if (email == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        User admin = userService.findUserByEmail(email);
        if (!admin.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access Denied!");
        }

        Author author = dtoToAuthor(new Author(), authorDTO);
        return authorRepository.save(author);
    }

    public void create(AuthorDTO authorDTO) {
        Author author = dtoToAuthor(new Author(), authorDTO);
        authorRepository.save(author);
    }

    public Author getAuthorByID(long authorID) {
        return authorRepository.findById(authorID)
                .orElseThrow(() -> new ResourceNotFoundException("Author with ID " + authorID + " has not found"));
    }

    public Author update(long authorID, AuthorDTO authorDTO, String email) {
        if (email == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        User admin = userService.findUserByEmail(email);
        if (!admin.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access Denied!");
        }
        Author author = dtoToAuthor(this.getAuthorByID(authorID), authorDTO);
        return authorRepository.save(author);
    }

    public String delete(long authorID, String email) {
        if (email == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        User admin = userService.findUserByEmail(email);
        if (!admin.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access Denied!");
        }
        Author author = this.getAuthorByID(authorID);
        for (Book book : bookService.getAllBooksByAuthor(author)) {
            bookService.setAuthorNull(book);
        }
        authorRepository.delete(author);
        return "Author " + author.getName() + " has been completely deleted.";
    }

    public void setImage(Media image, User user) {
        Author author = authorRepository.findAuthorByUser(user);
        if (author != null) {
            author.setImage(image);
            authorRepository.save(author);
        }
    }

    public Author setImage(Long authorID, MultipartFile multipartFile) throws IOException {
        Author author = this.getAuthorByID(authorID);

        Media image = cloudinaryService.createMediaFromMultipartFile(multipartFile);
        author.setImage(image);
        return authorRepository.save(author);
    }

    public void deleteImage(User user) {
        Author author = authorRepository.findAuthorByUser(user);
        author.setImage(null);
        authorRepository.save(author);
    }

    public String deleteImage(Long authorID) {
        Author author = this.getAuthorByID(authorID);
        author.setImage(null);
        authorRepository.save(author);
        return "Image successfully deleted";
    }

    public void updateRating(Book book) {
        long authorID = book.getID();
        Author author = getAuthorByID(authorID);

        double aveRating = (author.getAverageRating() + book.getAverageRating()) / 2;
        author.setAverageRating(aveRating);
        authorRepository.save(author);
    }
}