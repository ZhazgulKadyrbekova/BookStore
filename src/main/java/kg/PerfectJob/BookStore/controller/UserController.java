package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.dto.UserEditDTO;
import kg.PerfectJob.BookStore.dto.UserPasswordDTO;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/email/{email}")
    public User getByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }

    @PutMapping("/password")
    public User changePassword(@RequestBody UserPasswordDTO userPasswordDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return userService.changePassword(userPasswordDTO, principal.getName());
    }

    @GetMapping("/profile")
    public User getProfileInfo(Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return userService.findUserByEmail(principal.getName());
    }

    @PutMapping("/profile")
    public User updateProfileInfo(Principal principal, @RequestBody UserEditDTO userEditDTO) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return userService.updateUserByEmail(principal.getName(), userEditDTO);
    }

    @PutMapping("/image")
    public User setImage(@RequestParam("file") MultipartFile file, Principal principal)
        throws IOException {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return userService.setImage(file, principal.getName());
    }

    @DeleteMapping("/image")
    public ResponseMessage deleteImage(Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        return new ResponseMessage(userService.deleteImage(principal.getName()));
    }
}
