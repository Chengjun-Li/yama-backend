package team.yama.yama;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import team.yama.yama.domain.User;
import team.yama.yama.domain.UserType;
import team.yama.yama.repository.UserRepository;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        log.info("Initializing users data...");
        userRepository.save(
                User.builder()
                        .username("a")
                        .password(passwordEncoder.encode("1234"))
                        .email("a@example.com")
                        .userType(UserType.Admin)
                        .build());
        userRepository.save(
                User.builder()
                        .username("b")
                        .password(passwordEncoder.encode("1234"))
                        .email("b@example.com")
                        .userType(UserType.Tenant)
                        .build());

        log.info("Printing all users...");
        this.userRepository.findAll().forEach(v -> log.info(" User :" + v.toString()));
    }
}
