package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.AuthorDTO;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.Media;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.AccessDeniedException;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
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

    public List<Author> getAll(String type) {
        return authorRepository.findAllByTypeIgnoringCase(type);
    }

    private Author dtoToAuthor(Author author, AuthorDTO authorDTO) {
        author.setName(authorDTO.getName());
        author.setBirthDate(authorDTO.getBirthDate());
        author.setBiography(authorDTO.getBiography());

        return author;
    }

    public void createNewAuthor(User user) {
        Author author = new Author();
        author.setName(user.getName());
        //TODO create & use enum
        author.setType("NEW");
        authorRepository.save(author);
    }

    public Author createOldAuthor(AuthorDTO authorDTO, String email) {
        User admin = userService.findUserByEmail(email);
        if (!admin.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access Denied!");
        }

        Author author = dtoToAuthor(new Author(), authorDTO);
        //TODO create & use enum
        author.setType("OLD");
        return authorRepository.save(author);
    }

    public Author getAuthorByID(long authorID) {
        return authorRepository.findById(authorID)
                .orElseThrow(() -> new ResourceNotFoundException("Author with ID " + authorID + " has not found"));
    }

    public Author update(long authorID, AuthorDTO authorDTO, String email) {
        Author author = this.getAuthorByID(authorID);
        User admin = userService.findUserByEmail(email);
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            return authorRepository.save(dtoToAuthor(author, authorDTO));
        } else {
            throw new AccessDeniedException("Access Denied!");
        }

    }

    public String delete(long authorID, String email) {
        Author author = this.getAuthorByID(authorID);
        User admin = userService.findUserByEmail(email);
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            for (Book book : bookService.getAllBooksByAuthor(author)) {
                bookService.setAuthorNull(book);
            }
            authorRepository.delete(author);
            return "Author " + author.getName() + " has been completely deleted.";
        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public void setImage(Media image, User user) {
        Author author = authorRepository.findAuthorByUser(user);
        if (author != null) {
            author.setImage(image);
            authorRepository.save(author);
        }
    }

    public Author setImage(Long authorID, MultipartFile multipartFile, String email) throws IOException {
        Author author = this.getAuthorByID(authorID);
        User admin = userService.findUserByEmail(email);
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            Media image = cloudinaryService.createMediaFromMultipartFile(multipartFile);
            author.setImage(image);
            return authorRepository.save(author);
        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public void deleteImage(User user) {
        Author author = authorRepository.findAuthorByUser(user);
        author.setImage(null);
        authorRepository.save(author);
    }

    public String deleteImage(Long authorID, String email) {
        Author author = this.getAuthorByID(authorID);
        User admin = userService.findUserByEmail(email);
        if (admin.getRole().getName().equals("ROLE_ADMIN") || (author.getType().equals("NEW") && author.getUser().getEmail().equals(email))) {
            author.setImage(null);
            authorRepository.save(author);
            return "Image successfully deleted";
        } else {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    public void updateRating(Book book) {
        long authorID = book.getID();
        Author author = getAuthorByID(authorID);

        double aveRating = (author.getAverageRating() + book.getAverageRating()) / 2;
        author.setAverageRating(aveRating);
        authorRepository.save(author);
    }
}