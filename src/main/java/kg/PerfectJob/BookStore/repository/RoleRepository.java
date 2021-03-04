package kg.PerfectJob.BookStore.repository;

import kg.PerfectJob.BookStore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByNameContainingIgnoreCase(String name);
}
