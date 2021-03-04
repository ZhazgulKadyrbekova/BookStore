package kg.PerfectJob.BookStore.repository;

import kg.PerfectJob.BookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameContainingIgnoreCase(String name);
    User findByEmailContainingIgnoreCase(String email);
}
