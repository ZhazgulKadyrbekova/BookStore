package kg.PerfectJob.BookStore.service;

import kg.PerfectJob.BookStore.dto.UserAdminDTO;
import kg.PerfectJob.BookStore.dto.UserDTO;
import kg.PerfectJob.BookStore.dto.UserPasswordDTO;
import kg.PerfectJob.BookStore.dto.UserSaveAdminDTO;
import kg.PerfectJob.BookStore.entity.Role;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.InvalidDataException;
import kg.PerfectJob.BookStore.exception.InvalidInputException;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.repository.RoleRepository;
import kg.PerfectJob.BookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder encoder;

    public User findUserByID(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " has not found"));
    }

    public User findUserByName(String name) {
        return null;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public String createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName() + " " + userDTO.getSurname());
        user.setOccupation(userDTO.getOccupation());
	    user.setActivationCode(UUID.randomUUID().toString());
	    Role role = roleRepository.findByNameContainingIgnoreCase(user.getOccupation());
	    if (role == null) {
	        role = roleRepository.save(new Role("ROLE_" + userDTO.getOccupation().toUpperCase()));
        }
	    user.setRole(role);
        String message = "To activate your account visit link: register/activate/" + user.getActivationCode();
        if (mailService.send(user.getEmail(), "Activation of account", message)) {
            userRepository.save(user);
            return "Activation code has been successfully sent to email " + userDTO.getEmail();
        }
        return "We could not send activation code. Try again later.";
    }

    public String createAdmin(UserAdminDTO userAdminDTO) {
        User user = new User();
        user.setEmail(userAdminDTO.getEmail());
        Role role = roleRepository.findByNameContainingIgnoreCase(userAdminDTO.getRole());
        if (role == null)
            role = roleRepository.save(new Role("ROLE_" + userAdminDTO.getRole().toUpperCase()));
        user.setRole(role);
	    user.setActivationCode(UUID.randomUUID().toString());
        String message = "To activate your account visit link: register/activate/" + user.getActivationCode();
        if (mailService.send(user.getEmail(), "Activation of account", message)) {
            userRepository.save(user);
            return "Activation code has been sent to user's email " + userAdminDTO.getEmail();
        }
        return "We could not send activation code. Try again later.";
    }

    public User saveAdmin(UserSaveAdminDTO userSaveAdminDTO) {
        User user = userRepository.findByEmailIgnoreCase(userSaveAdminDTO.getEmail());
        if (!user.isActive())
            throw new InvalidInputException("Be sure to activate account at first");
        user.setPassword(userSaveAdminDTO.getPassword());
        user.setName(userSaveAdminDTO.getName() + " " + userSaveAdminDTO.getSurname());
        return userRepository.save(user);
    }

    public String activate(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null)
            return "Be sure to enter valid activation code";
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        return "Account has been activated";
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        String newPassword = UUID.randomUUID().toString().substring(0,10);
        user.setPassword(encoder.encode(newPassword));

        String message = "Your new password is: " + newPassword + ". You will be able to change it after logging in.";
        if (mailService.send(email, "Forgot password", message)) {
            userRepository.save(user);
            return "New password has been successfully sent to email: " + email;
        }
        return "We could not send new password to your email. Try again later.";
    }

    public void create(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User changePassword(UserPasswordDTO userPasswordDTO) {
        User user = userRepository.findByEmailIgnoreCase(userPasswordDTO.getEmail());
        if (user == null)
            throw new ResourceNotFoundException("User with email " + userPasswordDTO.getEmail() + " has not found");
        if (encoder.matches(user.getPassword(), userPasswordDTO.getOldPassword())) {
            user.setPassword(encoder.encode(userPasswordDTO.getNewPassword()));
            return userRepository.save(user);
        }
        else
            throw new InvalidDataException("Entered current password is not valid");
    }
}
