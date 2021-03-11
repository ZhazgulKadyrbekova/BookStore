package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.UserAdminDTO;
import kg.PerfectJob.BookStore.dto.UserDTO;
import kg.PerfectJob.BookStore.dto.UserSaveAdminDTO;
import kg.PerfectJob.BookStore.entity.User;

import java.util.List;

public interface UserService {
    User findUserByID(long id);
    User findUserByName(String name);
    User findUserByEmail(String email);
    List<User> findAll();
    String createUser(UserDTO userDTO);
    String createAdmin(UserAdminDTO userAdminDTO);
    User saveAdmin(UserSaveAdminDTO userSaveAdminDTO);
    String activate(String code);
    String forgotPassword(String email);
    void create(User user);
}
