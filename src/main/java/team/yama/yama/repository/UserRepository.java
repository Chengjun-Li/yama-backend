package team.yama.yama.repository;

import org.springframework.data.repository.CrudRepository;
import team.yama.yama.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}