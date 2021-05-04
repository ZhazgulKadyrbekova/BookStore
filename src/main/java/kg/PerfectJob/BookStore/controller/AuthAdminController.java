package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.UserAdminDTO;
import kg.PerfectJob.BookStore.dto.UserSaveAdminDTO;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Log4j2
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
        String email = principal.getName();
        log.info("User {} invited {} as {}", email, userAdminDTO.getEmail(), userAdminDTO.getRole());
        return userService.createAdmin(userAdminDTO, email);
    }

    @PostMapping("save")
    public User saveAdmin(@RequestBody UserSaveAdminDTO userDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return userService.saveAdmin(userDTO, principal.getName());
    }

}
