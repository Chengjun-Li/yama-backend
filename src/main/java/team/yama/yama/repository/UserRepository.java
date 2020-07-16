package team.yama.yama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.yama.yama.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}