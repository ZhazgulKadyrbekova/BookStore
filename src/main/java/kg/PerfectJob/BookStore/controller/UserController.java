package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.ResponseMessage;
import kg.PerfectJob.BookStore.dto.UserEditDTO;
import kg.PerfectJob.BookStore.dto.UserPasswordDTO;
import kg.PerfectJob.BookStore.entity.Author;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.service.AuthorService;
import kg.PerfectJob.BookStore.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthorService authorService;

    public UserController(UserService userService, AuthorService authorService) {
        this.userService = userService;
        this.authorService = authorService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/email/{email}")
    public User getByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }

    @GetMapping("/search")
    public Set<User> search(@RequestParam("name") String name) {
        return userService.search(name);
    }

    @PutMapping("/password")
    public User changePassword(@RequestBody UserPasswordDTO userPasswordDTO, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} changed password.", email);
        return userService.changePassword(userPasswordDTO, email);
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
        String email = principal.getName();
        log.info("User {} changed profile info.", email);
        return userService.updateUserByEmail(email, userEditDTO);
    }

    @PutMapping("/image")
    public User setImage(@RequestParam("file") MultipartFile file, Principal principal)
        throws IOException {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} set image.", email);
        return userService.setImage(file, email);
    }

    @DeleteMapping("/image")
    public ResponseMessage deleteImage(Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} deleted image.", email);
        return new ResponseMessage(userService.deleteImage(principal.getName()));
    }

    @DeleteMapping("/{userID}")
    public ResponseMessage blockUserByID(@PathVariable("userID") Long id, Principal principal) {
        if (principal == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        String email = principal.getName();
        log.info("User {} blocked user with id {}", email, id);
        return new ResponseMessage(userService.blockUserByID(id, email));
    }

    @GetMapping("/author/{userID}")
    public Author getAuthorByUserID(@PathVariable("userID") Long userID) {
        return authorService.getAuthorByUserID(userID);
    }
}
