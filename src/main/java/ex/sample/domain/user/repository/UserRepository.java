package ex.sample.domain.user.repository;

import ex.sample.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
