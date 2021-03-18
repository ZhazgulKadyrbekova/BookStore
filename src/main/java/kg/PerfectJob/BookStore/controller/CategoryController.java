package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.CategoryDTO;
import kg.PerfectJob.BookStore.entity.Category;
import kg.PerfectJob.BookStore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAll();
    }

    @GetMapping("/deleted/{deleted}")
    public List<Category> getAllCategoriesByDeleted(@PathVariable boolean deleted) {
        return categoryService.getAllByDeleted(deleted);
    }

    @PostMapping
    public Category createNewCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.create(categoryDTO);
    }

    @GetMapping("/{id}")
    public Category getCategoryByID(@PathVariable Long id) {
        return categoryService.getCategoryByID(id);
    }
}
