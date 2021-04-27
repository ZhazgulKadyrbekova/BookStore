package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/search")
public class GlobalSearchController {
    private final SearchService searchService;

    public GlobalSearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<Object> getAuthorsAndBooksByName(@RequestParam("name") String name) {
        return searchService.getAllByName(name);
    }
}
