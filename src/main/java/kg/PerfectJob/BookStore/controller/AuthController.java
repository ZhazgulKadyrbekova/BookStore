package kg.PerfectJob.BookStore.controller;

import kg.PerfectJob.BookStore.dto.*;
import kg.PerfectJob.BookStore.service.UserService;
import kg.PerfectJob.BookStore.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Log4j2
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
        log.info("User {} registered as {}", userDTO.getEmail(), userDTO.getOccupation());
        return new ResponseMessage(userService.createUser(userDTO));
    }

    @GetMapping("/activate/{code}")
    public UserResponseDTO activate(@PathVariable("code") String code) {
        return userService.activate(code);
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseMessage forgotPassword(@PathVariable String email) {
        log.info("User {} forgot password", email);
        return new ResponseMessage(userService.forgotPassword(email));
    }

    @PostMapping("/auth")
    public TokenDTO getToken(@RequestBody UserAuthDTO userAuthDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userAuthDTO.getEmail(), userAuthDTO.getPassword()));
        } catch (Exception e) {
            throw new Exception("Invalid credentials");
        }
        log.debug("User {} signed in", userAuthDTO.getEmail());
        return new TokenDTO(jwtUtil.generateToken(userAuthDTO.getEmail()));
    }
}
