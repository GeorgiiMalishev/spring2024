package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Project;
import my.spring2024.domain.Review;
import my.spring2024.domain.User;
import my.spring2024.infrastructure.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления проектами в приложении.
 */
@Service
@Slf4j
@Component
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Сохраняет проект в базе данных.
     * @param project проект
     * @return сохраненный проект
     */
    public Project saveProject(Project project) {
        log.info("Сохранен проект {}", project.getId());
        return projectRepository.save(project);
    }

    /**
     * Возвращает проект с заданым идентификатором
     * @param id Идентификатор проекта
     * @return проект; если проект не найден, то null
     */
    public Project getProjectById(long id) {
        Project project = projectRepository.findById(id);
        if (project == null)
            log.info("Не удалось найти проект с id {}", id);
        else {
            log.info("Проект с id {} найден", id);
            return project;
        }
        return null;
    }
}
