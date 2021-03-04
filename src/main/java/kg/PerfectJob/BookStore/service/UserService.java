package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.UserDTO;
import kg.PerfectJob.BookStore.entity.User;

import java.util.List;

public interface UserService {
    User findUserByID(long id);
    User findUserByName(String name);
    User findUserByEmail(String email);
    List<User> findAll();
    User create(UserDTO userDTO);
}
