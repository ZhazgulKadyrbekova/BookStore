package kg.PerfectJob.BookStore.repository;

import kg.PerfectJob.BookStore.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
