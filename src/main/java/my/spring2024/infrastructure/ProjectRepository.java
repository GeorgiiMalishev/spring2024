package my.spring2024.infrastructure;

import my.spring2024.domain.Post;
import my.spring2024.domain.Project;
import my.spring2024.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    public List<Project> findProjectsByName(String name);
    public Page<Project> findByNameContainingOrDescriptionContaining(String keyable, String keyable2, Pageable pageable);
}
