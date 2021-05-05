package kg.PerfectJob.BookStore.repository;

import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.Book;
import kg.PerfectJob.BookStore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByAuthorOrderByAverageRatingDesc(Author author);
    List<Book> findAllByCategory(Category category);
    List<Book> findAllByConfirmed(boolean confirmed);
    List<Book> findAllByNameContainingIgnoringCase(String name);
    List<Book> findTop10ByTypeIgnoringCaseOrderByAverageRatingDesc(String type);
}
