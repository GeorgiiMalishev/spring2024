package my.spring2024.infrastructure;

import my.spring2024.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    public List<Project> findProjectsByName(String name);
}
