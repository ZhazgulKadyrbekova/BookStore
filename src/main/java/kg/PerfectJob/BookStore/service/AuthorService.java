package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.AuthorDTO;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private UserService userService;
    @Autowired private BookService bookService;

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
}
