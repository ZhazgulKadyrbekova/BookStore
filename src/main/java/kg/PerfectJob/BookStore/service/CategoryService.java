package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.CategoryDTO;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.Category;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.AccessDeniedException;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookService bookService;
    private final UserService userService;

    public CategoryService(CategoryRepository categoryRepository, BookService bookService, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category create(CategoryDTO categoryDTO, String email) {
        User admin = userService.findUserByEmail(email);
        if (!admin.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access Denied!");
        }
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    public Category getCategoryByID(long categoryID) {
        return categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + categoryID + " has not found"));
    }

    public Category update(long categoryID, CategoryDTO categoryDTO, String email) {
        User admin = userService.findUserByEmail(email);
        if (!admin.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access Denied!");
        }
        Category category = this.getCategoryByID(categoryID);
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    public String delete(long categoryID, String email) {
        User admin = userService.findUserByEmail(email);
        if (!admin.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access Denied!");
        }
        Category category = this.getCategoryByID(categoryID);
        for (Book book : bookService.getAllBooksByCategory(category)) {
            bookService.setCategoryNull(book);
        }
        categoryRepository.delete(category);
        return "Category " + category.getName() + " has been completely deleted.";
    }

}
