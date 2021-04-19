package kg.PerfectJob.BookStore.boot;

import kg.PerfectJob.BookStore.entity.Role;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.repository.RoleRepository;
import kg.PerfectJob.BookStore.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public Init(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }


    @Override
    public void run(String... args) {
//        Role role = roleRepository.save(new Role("ROLE_ADMIN"));
//        userService.create(new User("admin@gmail.com", "***REMOVED***", "Admin", null,
//                true, null, role, null));
    }
}
