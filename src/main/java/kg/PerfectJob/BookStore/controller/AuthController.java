package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.*;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.service.UserService;
import kg.PerfectJob.BookStore.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/register")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/user")
    public ResponseMessage createUser(@RequestBody UserDTO userDTO) {
        return new ResponseMessage(userService.createUser(userDTO));
    }

    @PostMapping("/admin")
    public String createAdmin(@RequestBody UserAdminDTO userAdminDTO) {
        return userService.createAdmin(userAdminDTO);
    }

    @PostMapping("/saveAdmin")
    public User saveAdmin(@RequestBody UserSaveAdminDTO userDTO) {
        return userService.saveAdmin(userDTO);
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable("code") String code) {
        return userService.activate(code);
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseMessage forgotPassword(@PathVariable String email) {
        return new ResponseMessage(userService.forgotPassword(email));
    }

    @PostMapping("/auth")
    public TokenDTO getToken(@RequestBody UserAuthDTO userAuthDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userAuthDTO.getEmail(), userAuthDTO.getPassword()));
        } catch (Exception e) {
            throw new Exception("Invalid data");
        }
        return new TokenDTO(jwtUtil.generateToken(userAuthDTO.getEmail()));
    }
}
