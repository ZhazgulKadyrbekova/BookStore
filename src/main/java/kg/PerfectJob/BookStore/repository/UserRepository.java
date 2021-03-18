package kg.PerfectJob.BookStore.repository;

import kg.PerfectJob.BookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameContainingIgnoreCase(String name);
//    User findByEmailContainingIgnoreCase(String email);
    User findByEmailIgnoreCase(String email);
    User findByActivationCode(String code);
    List<User> findAllByDeleted(boolean deleted);
}
