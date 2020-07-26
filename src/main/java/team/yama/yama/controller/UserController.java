package team.yama.yama.controller;

import org.apache.tomcat.util.descriptor.web.ContextHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import team.yama.yama.domain.User;
import team.yama.yama.domain.UserType;
import team.yama.yama.repository.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    admin can search info of any type of user
    employee can only search info of tenant
    manager can only search info of work under his management
     */
    @PreAuthorize("hasAnyAuthority('Admin', 'Employee', 'Manager')")
    @GetMapping("/users/{username}")
    ResponseEntity<User> get(@PathVariable String username) {
        //get userType of user who sent the search request
        String userType = findType();
        //get targetType of the target the user want to search
        User target = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        String targetType = findType(target);
        //check if user has the authority to view target info
        if (userType.equals("Admin") || (userType.equals("Employee") && targetType.equals("Tenant"))
                    || (userType.equals("Manager") && targetType.equals("Worker"))) {
            return ok(target);
        } else {
            return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
        }
    }
    /*
    admin can see all user info
    employee can only see all tenant info
    manager can only see all work info
     */
    @PreAuthorize("hasAnyAuthority('Admin','Employee','Manager')")
    @GetMapping("/users/all")
    ResponseEntity<List<User>> get(){
        String userType = findType();
        List<User> users = (List<User>) userRepository.findAll();
        if (userType.equals("Admin")) {
            return ok(users);
        } else if (userType.equals("Employee")){
            List<User> tenants = new ArrayList<>();
            for (User user : users) {
                if (user.getUserType().equals(UserType.Tenant)){
                    tenants.add(user);
                }
            }
            return ok(tenants);
        } else {
            List<User> workers = new ArrayList<>();
            for (User user : users) {
                if (user.getUserType().equals(UserType.Worker)){
                    workers.add(user);
                }
            }
            return ok(workers);
        }
    }
    /*
    admin can add user of any type
    employee can only add user of tenant
    manager can only add user of worker
     */
    @PreAuthorize("hasAnyAuthority('Admin','Employee','Manager')")
    @PostMapping("/users")
    ResponseEntity<User> save(@RequestBody User newUser) {
        //get userType of user who sent the search request
        String userType = findType();
        //get targetType of the target the user want to search
        String targetType = findType(newUser);
        //check if user has the authority to view target info
        if (userType.equals("Admin") || (userType.equals("Employee") && targetType.equals("Tenant"))
                || (userType.equals("Manager") && targetType.equals("Worker"))) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            User saved = userRepository.save(newUser);
            return ok(saved);
        } else {
            return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
        }
    }


    @PutMapping("/users/{username}")
    ResponseEntity<User> update(@RequestBody User newUser, @PathVariable String username) {
        String userType = findType();
        Long id = findId();
        User existed = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        String targetType = findType(existed);
        if (userType.equals("Admin") || (userType.equals("Manager") && targetType.equals("Worker"))
                      || (userType.equals(targetType) && id == existed.getId())){
            existed.setUsername(newUser.getUsername());
            existed.setPassword(passwordEncoder.encode(newUser.getPassword()));
            existed.setEmail(newUser.getEmail());
            existed.setFirstName(newUser.getFirstName());
            existed.setLastName(newUser.getLastName());
            existed.setUnit(newUser.getUnit());
            existed.setPhoneNumber(newUser.getPhoneNumber());
            User saved = userRepository.save(existed);
            return ok(saved);
        } else {
            return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
        }
    }


    @DeleteMapping("/users/{username}")
    ResponseEntity<User> delete(@PathVariable String username) {
        String userType = findType();
        Long id = findId();
        User existed = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        String targetType = findType(existed);
        if (userType.equals("Admin") || (userType.equals("Manager") && targetType.equals("Worker"))
                || (userType.equals(targetType) && id == existed.getId())) {
            userRepository.delete(existed);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
        }
    }

    private String findType() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList()).get(0);
    }
    private String findType(User user) {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList()).get(0);
    }
    private Long findId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user.getId();
    }
}
