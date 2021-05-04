package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.AuthorDTO;
import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.exception.InvalidInputException;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.AuthorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAll();
    }

    @GetMapping("/type")
    public List<Author> getAllAuthorsByType(@RequestParam("type") String type) {
        if (!type.equalsIgnoreCase("NEW") && !type.equalsIgnoreCase("OLD"))
            throw new InvalidInputException("Be sure to send 'new' or 'old' types");
        return authorService.getAll(type);
    }

    @PostMapping
    public Author createAuthor(@RequestBody AuthorDTO authorDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} created new author {}", email, authorDTO);
        return authorService.createOldAuthor(authorDTO, email);
    }

    @GetMapping("/{id}")
    public Author getAuthorByID(@PathVariable Long id) {
        return authorService.getAuthorByID(id);
    }

    @PutMapping("/{id}")
    public Author updateAuthorInfo(@PathVariable("id") Long authorID, @RequestBody AuthorDTO authorDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} updated author info:\n\tid: {}\n\tinfo: {}", email, authorID, authorDTO);
        return authorService.update(authorID, authorDTO, email);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteAuthorByID(@PathVariable("id") Long authorID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} deleted author id: {}", email, authorID);
        return new ResponseMessage(authorService.delete(authorID, email));
    }

    @PutMapping("/{authorID}/image")
    public Author setImage(@PathVariable Long authorID, @RequestParam("file") MultipartFile file,
                           Principal principal) throws IOException {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} set image to author id: {}", email, authorID);
        return authorService.setImage(authorID, file, email);
    }

    @DeleteMapping("/{authorID}/image")
    public ResponseMessage deleteImage(@PathVariable Long authorID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} deleted image of author id: {}", email, authorID);
        return new ResponseMessage(authorService.deleteImage(authorID, email));
    }
}
