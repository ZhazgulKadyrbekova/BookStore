package kg.PerfectJob.BookStore.boot;

import kg.PerfectJob.BookStore.entity.Role;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.repository.RoleRepository;
import kg.PerfectJob.BookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class Init implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {
//        Role role = roleRepository.save(new Role(0, "ROLE_ADMIN"));
  //      userRepository.save(new User(0, "admin@gmail.com", "12345678", "Admin", null, true, null, role));
    }
}
