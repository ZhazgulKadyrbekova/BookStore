package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.AuthorDTO;
import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.service.AuthorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/deleted/{deleted}")
    public List<Author> getAllAuthorsByDeleted(@PathVariable boolean deleted) {
        return authorService.getAllByDeleted(deleted);
    }

    @PostMapping
    public Author createNewAuthor(@RequestBody AuthorDTO authorDTO) {
        return authorService.create(authorDTO);
    }

    @GetMapping("/{id}")
    public Author getAuthorByID(@PathVariable Long id) {
        return authorService.getAuthorByID(id);
    }

    @PutMapping("/{id}")
    public Author updateAuthorInfo(@PathVariable("id") Long authorID, @RequestBody AuthorDTO authorDTO) {
        return authorService.update(authorID, authorDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteAuthorByID(@PathVariable("id") Long authorID) {
        return new ResponseMessage(authorService.delete(authorID));
    }

    @PutMapping("/unarchive/{id}")
    public Author unarchiveAuthorByID(@PathVariable("id") Long authorID) {
        return authorService.unarchive(authorID);
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
