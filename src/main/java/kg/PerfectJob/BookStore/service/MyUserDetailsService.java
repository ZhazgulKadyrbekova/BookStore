package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.entity.Role;
import kg.PerfectJob.BookStore.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        kg.PerfectJob.BookStore.entity.User user = userRepository.findByEmailIgnoreCase(email);
        List<Role> roles = new ArrayList<>();
        roles.add(user.getRole());
        return new User(user.getEmail(), user.getPassword(), roles);
    }
}
