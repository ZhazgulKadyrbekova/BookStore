package kg.PerfectJob.BookStore.boot;

import kg.PerfectJob.BookStore.entity.Role;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.repository.RoleRepository;
import kg.PerfectJob.BookStore.repository.UserRepository;
import kg.PerfectJob.BookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    @Autowired private UserService userService;
    @Autowired private RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {
//        Role role = roleRepository.save(new Role("ROLE_ADMIN"));
//        userService.create(new User("admin@gmail.com", "12345678", "Admin", null, true, null, role));
    }
}
