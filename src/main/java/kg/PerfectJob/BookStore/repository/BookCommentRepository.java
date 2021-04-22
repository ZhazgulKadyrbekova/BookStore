package kg.PerfectJob.BookStore.repository;

import kg.PerfectJob.BookStore.entity.BookComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
}
