package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.UserAdminDTO;
import kg.PerfectJob.BookStore.dto.UserSaveAdminDTO;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/register/admin")
public class AuthAdminController {
    private final UserService userService;

    public AuthAdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String createAdmin(@RequestBody UserAdminDTO userAdminDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return userService.createAdmin(userAdminDTO, principal.getName());
    }

    @PostMapping("save")
    public User saveAdmin(@RequestBody UserSaveAdminDTO userDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return userService.saveAdmin(userDTO, principal.getName());
    }

}
