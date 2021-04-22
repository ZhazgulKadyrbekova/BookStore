package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.AuthorDTO;
import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.AuthorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

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

    @PostMapping
    public Author createNewAuthor(@RequestBody AuthorDTO authorDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return authorService.create(authorDTO, principal.getName());
    }

    @GetMapping("/{id}")
    public Author getAuthorByID(@PathVariable Long id) {
        return authorService.getAuthorByID(id);
    }

    @PutMapping("/{id}")
    public Author updateAuthorInfo(@PathVariable("id") Long authorID, @RequestBody AuthorDTO authorDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return authorService.update(authorID, authorDTO, principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteAuthorByID(@PathVariable("id") Long authorID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return new ResponseMessage(authorService.delete(authorID, principal.getName()));
    }

    @PutMapping("/{authorID}/image")
    public Author setImage(@PathVariable Long authorID, @RequestParam("file") MultipartFile file)
            throws IOException {
        return authorService.setImage(authorID, file);
    }

    @DeleteMapping("/{authorID}/image")
    public ResponseMessage deleteImage(@PathVariable Long authorID) {
        return new ResponseMessage(authorService.deleteImage(authorID));
    }
}
