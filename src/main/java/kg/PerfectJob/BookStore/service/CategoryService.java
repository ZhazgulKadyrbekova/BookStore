package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.CategoryDTO;
import kg.PerfectJob.BookStore.entity.Category;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category create(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    public List<Category> getAllByDeleted(boolean deleted) {
        return categoryRepository.findAllByDeleted(deleted);
    }

    public Category getCategoryByID(long categoryID) {
        return categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + categoryID + " has not found"));
    }
}
