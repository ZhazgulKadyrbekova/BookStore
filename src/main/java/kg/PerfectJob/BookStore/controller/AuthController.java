package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.UserDTO;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/register")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User register(@RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }
}
