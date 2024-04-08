package my.spring2024.infrastructure;

import java.util.List;

import my.spring2024.domain.Review;
import my.spring2024.domain.TeamRoleTag;
import my.spring2024.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String name);
    List<User> findAllByRole(TeamRoleTag role);
    List<User> findAllByCurrentProjects_Id(Long projectId);
    List<User> findAllByPastProjects_Id(Long projectId);
}
