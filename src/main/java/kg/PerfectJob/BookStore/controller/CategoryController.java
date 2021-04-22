package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.CategoryDTO;
import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.entity.Category;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/deleted/{deleted}")
    public List<Category> getAllCategoriesByDeleted(@PathVariable boolean deleted) {
        return categoryService.getAllByDeleted(deleted);
    }

    @PostMapping
    public Category createNewCategory(@RequestBody CategoryDTO categoryDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return categoryService.create(categoryDTO, principal.getName());
    }

    @GetMapping("/{id}")
    public Category getCategoryByID(@PathVariable Long id) {
        return categoryService.getCategoryByID(id);
    }

    @PutMapping("/{id}")
    public Category updateCategoryInfo(@PathVariable("id") Long categoryID, @RequestBody CategoryDTO categoryDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return categoryService.update(categoryID, categoryDTO, principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteCategoryByID(@PathVariable("id") Long categoryID, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return new ResponseMessage(categoryService.delete(categoryID, principal.getName()));
    }

    @PutMapping("/unarchive/{id}")
    public Category unarchiveCategoryByID(@PathVariable("id") Long categoryID) {
        return categoryService.unarchive(categoryID);
    }
}
