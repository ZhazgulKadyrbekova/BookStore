package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.CategoryDTO;
import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Category;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAll();
    }

    @PostMapping
    public Category createNewCategory(@RequestBody CategoryDTO categoryDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} created new category info: {}", email, categoryDTO);
        return categoryService.create(categoryDTO, email);
    }

    @GetMapping("/{id}")
    public Category getCategoryByID(@PathVariable Long id) {
        return categoryService.getCategoryByID(id);
    }

    @PutMapping("/{id}")
    public Category updateCategoryInfo(@PathVariable("id") Long categoryID, @RequestBody CategoryDTO categoryDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} updated category id: {}\n\tinfo: {}", email, categoryID, categoryDTO);
        return categoryService.update(categoryID, categoryDTO, email);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteCategoryByID(@PathVariable("id") Long categoryID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} deleted category id: {}", email, categoryID);
        return new ResponseMessage(categoryService.delete(categoryID, principal.getName()));
    }

}
