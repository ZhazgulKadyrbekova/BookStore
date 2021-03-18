package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.AuthorDTO;
import kg.PerfectJob.BookStore.entity.Author;
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

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public List<Author> getAllByDeleted(boolean deleted) {
        return authorRepository.findAllByDeleted(deleted);
    }

    public Author create(AuthorDTO authorDTO) {
        Author author = new Author();
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

        return authorRepository.save(author);
    }

    public Author getAuthorByID(long authorID) {
        return authorRepository.findById(authorID)
                .orElseThrow(() -> new ResourceNotFoundException("Author with ID " + authorID + " has not found"));
    }
}
