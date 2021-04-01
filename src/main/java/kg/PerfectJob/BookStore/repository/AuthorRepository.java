package kg.PerfectJob.BookStore.repository;

import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAllByDeleted(boolean deleted);
    Author findAuthorByUser(User user);
}
