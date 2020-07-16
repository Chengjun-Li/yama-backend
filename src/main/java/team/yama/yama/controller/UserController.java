package team.yama.yama.controller;

import org.springframework.http.ResponseEntity;
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

    @GetMapping("/users/{id}")
    ResponseEntity<User> get(@PathVariable Long id) {
        return ok(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @PostMapping("/users")
    ResponseEntity<User> save(@RequestBody User newUser) {
        User saved = userRepository.saveAndFlush(newUser);
        return ok(saved);
    }

    @PutMapping("/users/{id}")
    ResponseEntity<User> update(@RequestBody User newUser, @PathVariable Long id) {
        User existed = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        existed.setEmail(newUser.getEmail());
        existed.setPassword(newUser.getPassword());
        existed.setUserType(newUser.getUserType());
        existed.setFirstName(newUser.getFirstName());
        existed.setLastName(newUser.getLastName());
        User saved = userRepository.saveAndFlush(existed);
        return ok(saved);
    }

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable Long id) {
        User existed = userRepository.findById(id).orElseThrow((() -> new UserNotFoundException(id)));
        userRepository.delete(existed);
    }

}
