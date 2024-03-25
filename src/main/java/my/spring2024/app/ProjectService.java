package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Project;
import my.spring2024.infrastructure.ProjectRepository;
import org.springframework.stereotype.Service;


/**
 * Сервис для управления проектами в приложении.
 */
@Service
@Slf4j
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
        var savedProject = projectRepository.save(project);
        log.info("Сохранен проект {}", project.getId());
        return savedProject;
    }

    /**
     * Возвращает проект с заданым идентификатором
     * @param id Идентификатор проекта
     * @return проект; если проект не найден, то null
     */
    public Project getProjectById(Long id) {
        var project = projectRepository.findById(id);
        if (project.isEmpty()){
            log.info("Не удалось найти проект с id {}", id);
            return null;
        } else {
            log.info("Проект с id {} найден", id);
            return project.get();
        }
    }

    /**
     * Удаляет проект из базы данных по идентификатору.
     * @param id Идентификатор отзыва.
     */
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
        log.info("Удален проект с id {}", id);
    }
}
