package kg.PerfectJob.BookStore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.PerfectJob.BookStore.dto.AuthorDTO;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.Image;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.AuthorRepository;
import kg.PerfectJob.BookStore.repository.ImageRepository;
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
    private final UserService userService;
    private final BookService bookService;
    private final ImageRepository imageRepository;

    public AuthorService(AuthorRepository authorRepository, UserService userService, BookService bookService, ImageRepository imageRepository) {
        this.authorRepository = authorRepository;
        this.userService = userService;
        this.bookService = bookService;
        this.imageRepository = imageRepository;
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
        if (authorDTO.getUserID() != 0) {
            User user = userService.findUserByID(authorDTO.getUserID());
            if (user == null)
                throw new ResourceNotFoundException("User with ID " + authorDTO.getUserID() + " has not found");
            author.setUser(user);
        }

        return author;
    }

    public Author create(AuthorDTO authorDTO) {
        Author author = dtoToAuthor(new Author(), authorDTO);
        return authorRepository.save(author);
    }

    public Author getAuthorByID(long authorID) {
        return authorRepository.findById(authorID)
                .orElseThrow(() -> new ResourceNotFoundException("Author with ID " + authorID + " has not found"));
    }

    public Author update(long authorID, AuthorDTO authorDTO) {
        Author author = dtoToAuthor(this.getAuthorByID(authorID), authorDTO);
        return authorRepository.save(author);
    }

    public String delete(long authorID) {
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

    public void setImage(Image image, User user) {
        Author author = authorRepository.findAuthorByUser(user);
        if (author != null) {
            author.setImage(image);
            authorRepository.save(author);
        }
    }

    public Author setImage(Long authorID, MultipartFile multipartFile) throws IOException {

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
