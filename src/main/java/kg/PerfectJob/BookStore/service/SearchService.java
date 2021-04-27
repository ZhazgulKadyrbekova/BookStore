package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.repository.AuthorRepository;
import kg.PerfectJob.BookStore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SearchService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public SearchService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<Object> getAllByName(String name) {
        List<Object> result = new java.util.ArrayList<>(Collections.singletonList(authorRepository.findAllByNameContaining(name)));
        result.addAll(bookRepository.findAllByNameContaining(name));

        return result;
    }
}
