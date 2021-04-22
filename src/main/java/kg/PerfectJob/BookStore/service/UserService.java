package kg.PerfectJob.BookStore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.PerfectJob.BookStore.dto.*;
import kg.PerfectJob.BookStore.entity.Media;
import kg.PerfectJob.BookStore.entity.Role;
import kg.PerfectJob.BookStore.entity.User;
import kg.PerfectJob.BookStore.exception.AccessDeniedException;
import kg.PerfectJob.BookStore.exception.InvalidInputException;
import kg.PerfectJob.BookStore.exception.ResourceNotFoundException;
import kg.PerfectJob.BookStore.exception.UnauthorizedException;
import kg.PerfectJob.BookStore.repository.MediaRepository;
import kg.PerfectJob.BookStore.repository.RoleRepository;
import kg.PerfectJob.BookStore.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final PasswordEncoder encoder;
    private final MediaRepository imageRepository;
    private final AuthorService authorService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, MailService mailService, PasswordEncoder encoder,
                       MediaRepository imageRepository, @Lazy AuthorService authorService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mailService = mailService;
        this.encoder = encoder;
        this.imageRepository = imageRepository;
        this.authorService = authorService;
    }

    public User findUserByID(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " has not found"));
    }

    public User findUserByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
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

    public String createAdmin(UserAdminDTO userAdminDTO, String email) {
        if (email == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        User admin = findUserByEmail(email);
        if (!admin.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access Denied!");
        }
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

    public User saveAdmin(UserSaveAdminDTO userSaveAdminDTO, String email) {
        if (email == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        User admin = findUserByEmail(email);
        if (!admin.getRole().getName().equals("ROLE_ADMIN") || !admin.getRole().getName().equals("ROLE_MODERATOR")) {
            throw new AccessDeniedException("Access Denied!");
        }
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
        if (user.getOccupation().equals("writer")) {
            AuthorDTO author = new AuthorDTO();
            author.setName(user.getName());
            author.setType("NEW");
            authorService.create(author);
        }
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

    public User changePassword(UserPasswordDTO userPasswordDTO, String email) {
        if (email == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        if (!email.equals(userPasswordDTO.getEmail())) {
            throw new AccessDeniedException("Access Denied!");
        }
        User user = userRepository.findByEmailIgnoreCase(userPasswordDTO.getEmail());
        if (user == null)
            throw new ResourceNotFoundException("User with email " + userPasswordDTO.getEmail() + " has not found");
        if (encoder.matches(userPasswordDTO.getOldPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(userPasswordDTO.getNewPassword()));
            return userRepository.save(user);
        }
        else
            throw new AccessDeniedException("Entered current password is not valid");
    }

    public User setImage(MultipartFile multipartFile, String userEmail) throws IOException {

        final String urlKey = "cloudinary://122578963631996:RKDo37y7ru4nnuLsBGQbwBUk65o@zhazgul/"; //в конце добавляем '/'
        Media image = new Media();
        File file;
        try{
            file = Files.createTempFile(System.currentTimeMillis() + "",
                    Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().length()-4)) // .jpg
                    .toFile();
            multipartFile.transferTo(file);

            Cloudinary cloudinary = new Cloudinary(urlKey);
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            image.setName((String) uploadResult.get("public_id"));
            image.setUrl((String) uploadResult.get("url"));
            image.setFormat((String) uploadResult.get("format"));
            imageRepository.save(image);

            User user = userRepository.findByEmailIgnoreCase(userEmail);
            user.setImage(image);
            userRepository.save(user);
            authorService.setImage(image, user);
            return user;
        } catch (IOException e){
            throw new IOException("Unable to set image to user\n" + e.getMessage());
        }
    }

    public String deleteImage(String email) {
        if (email == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        User user = userRepository.findByEmailIgnoreCase(email);
        user.setImage(null);
        userRepository.save(user);
        authorService.deleteImage(user);

        return "Image successfully deleted";
    }

    public User updateUserByEmail(String email, UserEditDTO userEditDTO) {
        if (email == null)
            throw new UnauthorizedException("Please, authorize to see the response");
        User user = this.findUserByEmail(email);
        user.setName(userEditDTO.getName() + " " + userEditDTO.getSurname());
        return userRepository.save(user);
    }
}
