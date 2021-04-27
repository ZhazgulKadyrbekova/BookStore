package kg.PerfectJob.BookStore.repository;

import kg.PerfectJob.BookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Set<User> findAllByNameContainingIgnoringCase(String name);
    Set<User> findAllByEmailContainingIgnoringCase(String email);
    User findByEmailIgnoreCase(String email);
    User findByActivationCode(String code);
}
