package kg.PerfectJob.BookStore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.PerfectJob.BookStore.dto.AuthorDTO;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.Media;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.AccessDeniedException;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.repository.AuthorRepository;
import kg.PerfectJob.BookStore.repository.MediaRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final MediaRepository imageRepository;
    private final UserService userService;

    public AuthorService(AuthorRepository authorRepository, BookService bookService,
                         MediaRepository imageRepository, @Lazy UserService userService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public List<Author> getAllByDeleted(boolean deleted) {
        return authorRepository.findAllByDeleted(deleted);
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
        if (author.isDeleted()) {
            for (Book book : bookService.getAllBooksByAuthor(author)) {
                bookService.setAuthorNull(book);
            }
            authorRepository.delete(author);
            return "Author " + author.getName() + " has been completely deleted.";
        } else {
            author.setDeleted(true);
            authorRepository.save(author);
            return "Author " + author.getName() + " has been archived.";
        }
    }

    public Author unarchive(long authorID) {
        Author author = this.getAuthorByID(authorID);
        author.setDeleted(false);
        return authorRepository.save(author);
    }

    public void setImage(Media image, User user) {
        Author author = authorRepository.findAuthorByUser(user);
        if (author != null) {
            author.setImage(image);
            authorRepository.save(author);
        }
    }

    public Author setImage(Long authorID, MultipartFile multipartFile) throws IOException {

        final String urlKey = "cloudinary://122578963631996:RKDo37y7ru4nnuLsBGQbwBUk65o@zhazgul/"; //в конце добавляем '/'
        Media image = new Media();
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

            Author author = this.getAuthorByID(authorID);
            author.setImage(image);
            return authorRepository.save(author);
        } catch (IOException e){
            throw new IOException("Unable to set image to author\n" + e.getMessage());
        }
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
}

/*
2021-04-22 23:31:37.454 ERROR 5249 --- [nio-8080-exec-3] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception

io.jsonwebtoken.SignatureException: JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.
*/