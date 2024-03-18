package my.spring2024.infrastructure;

import my.spring2024.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    User findById(Long id);
}
