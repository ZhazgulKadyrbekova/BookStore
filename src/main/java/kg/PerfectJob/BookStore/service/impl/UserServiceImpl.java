package kg.PerfectJob.BookStore.service.impl;

import kg.PerfectJob.BookStore.dto.UserDTO;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.UserRepository;
import kg.PerfectJob.BookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User findUserByID(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " has not found"));
    }

    @Override
    public User findUserByName(String name) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName() + " " + userDTO.getSurname());
        user.setOccupation(userDTO.getOccupation());
        return userRepository.save(user);
    }
}
