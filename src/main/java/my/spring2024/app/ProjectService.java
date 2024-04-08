package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Project;
import my.spring2024.domain.Review;
import my.spring2024.domain.User;
import my.spring2024.app.UserService;
import my.spring2024.infrastructure.ProjectRepository;
import my.spring2024.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


/**
 * Сервис для управления проектами в приложении.
 */
@Service
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
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

    /**
     * Добавляет пользователя в проект.
     *
     * @param projectId Идентификатор проекта.
     * @param user Пользователь, которого нужно добавить.
     * @return Обновленный проект с добавленным пользователем.
     */
    public Project addUserToProject(Long projectId, User user) {
        Project project = getProjectById(projectId);
        if (project != null) {
            project.getUsers().add(user);
            log.info("Добавление пользователя {} в проект с id {}", user.getId(), projectId);
            return saveProject(project);
        }
        return null;
    }

    /**
     * Удаляет пользователя из проекта.
     *
     * @param projectId Идентификатор проекта.
     * @param user Пользователь, которого нужно удалить.
     * @return Обновленный проект без удаленного пользователя.
     */
    public Project removeUserFromProject(Long projectId, User user) {
        Project project = getProjectById(projectId);
        if (project == null) {
            log.info("Не удалось удалить пользователя {} из проекта с id {}: проект не найден", user.getId(), projectId);
            return null;
        }

        if (!project.getUsers().contains(user)) {
            log.info("Пользователь {} не найден в проекте {} при попытке удаления", user.getId(), projectId);
            return null;
        }

        userService.moveProjectToPast(user, project);
        project.getUsers().remove(user);
        log.info("Удаление пользователя {} из проекта с id {}", user.getId(), projectId);
        return saveProject(project);
    }

    /**
     * Добавляет отзыв к проекту.
     *
     * @param projectId Идентификатор проекта.
     * @param review Отзыв, который нужно добавить.
     * @return Обновленный проект с добавленным отзывом.
     */
    public Project addReviewToProject(Long projectId, Review review) {
        Project project = getProjectById(projectId);
        if (project == null) {
            log.info("Не удалось добавить отзыв {} к проекту с id {}: проект не найден", review.getId(), projectId);
            return null;
        }

        project.getReviews().add(review);
        log.info("Добавление отзыва {} к проекту с id {}", review.getId(), projectId);
        return saveProject(project);
    }

    /**
     * Удаляет отзыв из проекта.
     *
     * @param projectId Идентификатор проекта.
     * @param review Отзыв, который нужно удалить.
     * @return Обновленный проект без удаленного отзыва.
     */
    public Project removeReviewFromProject(Long projectId, Review review) {
        Project project = getProjectById(projectId);
        if (project == null) {
            log.info("Не удалось удалить отзыв {} из проекта с id {}: проект не найден", review.getId(), projectId);
            return null;
        }

        if (!project.getReviews().contains(review)) {
            log.info("Отзыв {} не найден в проекте {} при попытке удаления", review.getId(), projectId);
            return null;
        }

        project.getReviews().remove(review);
        log.info("Удаление отзыва {} из проекта с id {}", review.getId(), projectId);
        return saveProject(project);
    }
}

