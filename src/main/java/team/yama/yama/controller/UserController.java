package team.yama.yama.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import team.yama.yama.domain.User;
import team.yama.yama.repository.UserRepository;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAnyAuthority('Admin', 'Employee', 'Manager')")
    @GetMapping("/users/{username}")
    ResponseEntity<User> get(@PathVariable String username) {
        return ok(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username)));
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee','Manager')")
    @PostMapping("/users")
    ResponseEntity<User> save(@RequestBody User newUser) {
        User saved = userRepository.save(newUser);
        return ok(saved);
    }


    @PutMapping("/users/{username}")
    ResponseEntity<User> update(@RequestBody User newUser, @PathVariable String username) {
        User existed = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        existed.setUsername(newUser.getUsername());
        existed.setPassword(newUser.getPassword());
        existed.setEmail(newUser.getEmail());
        existed.setUserType(newUser.getUserType());
        existed.setFirstName(newUser.getFirstName());
        existed.setLastName(newUser.getLastName());
        User saved = userRepository.save(existed);
        return ok(saved);
    }


    @DeleteMapping("/users/{username}")
    void delete(@PathVariable String username) {
        User existed = userRepository.findByUsername(username).orElseThrow((() -> new UserNotFoundException(username)));
        userRepository.delete(existed);
    }

}
