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
        if (project == null) {
            log.info("Не удалось добавить пользователя {} к проекту с id {}: проект не найден", user.getId(), projectId);
            return null;
        }

        if (!project.getUsers().contains(user)) {
            project.getUsers().add(user);
            log.info("Добавление пользователя {} в проект с id {}", user.getId(), projectId);
            return saveProject(project);
        }

        log.info("Пользователь {} уже присутствует в проекте с id {}", user.getId(), projectId);
        return project;
    }

    /**
     * Удаляет пользователя из проекта.
     * Требуется, чтобы вызывающий пользователь был лидером проекта.
     *
     * @param projectId Идентификатор проекта.
     * @param user Пользователь, которого нужно удалить.
     * @param initiator Пользователь, запрашивающий операцию удаления.
     * @return Обновленный проект без удаленного пользователя, или null, если проект не найден.
     */
    public Project removeUserFromProject(Long projectId, User user, User initiator) {
        Project project = getProjectById(projectId);
        if (project == null) return null;

        if (!initiator.equals(user) && !project.getLeader().equals(initiator)) {
            log.info("Пользователь {} не удален из проекта {}, так как у инициатора {} нет прав", user.getId(), projectId, initiator.getId());
            return project;
        }

        if (project.getUsers().contains(user)) {
            project.getUsers().remove(user);
            log.info("Удаление пользователя {} из проекта с id {}", user.getId(), projectId);
            return saveProject(project);
        }

        log.info("Пользователь {} не найден в проекте {} при попытке удаления", user.getId(), projectId);
        return project;
    }


    /**
     * Добавляет отзыв к проекту.
     * Проект должен существовать для успешного добавления отзыва.
     *
     * @param projectId Идентификатор проекта.
     * @param review Отзыв, который нужно добавить.
     * @return Обновленный проект с добавленным отзывом, или null, если проект не найден.
     */
    public Project addReviewToProject(Long projectId, Review review) {
        Project project = getProjectById(projectId);
        if (project == null) {
            log.info("Не удалось добавить отзыв {} к проекту с id {}: проект не найден", review.getId(), projectId);
            return null;
        }

        if (!project.getReviews().contains(review)) {
            project.getReviews().add(review);
            log.info("Добавление отзыва {} к проекту с id {}", review.getId(), projectId);
            return saveProject(project);
        }

        log.info("Не удалось добавить отзыв {} к проекту с id {}: в проекте уже имеется этот отзыв", review.getId(), projectId);
        return project;
    }

    /**
     * Удаляет отзыв из проекта.
     * Требуется, чтобы вызывающий пользователь был автором отзыва.
     *
     * @param projectId Идентификатор проекта.
     * @param review Отзыв, который нужно удалить.
     * @param user Пользователь, запрашивающий операцию удаления.
     * @return Обновленный проект без удаленного отзыва, или null, если проект не найден.
     */
    public Project removeReviewFromProject(Long projectId, Review review, User user) {
        Project project = getProjectById(projectId);
        if (project == null) return null;

        if (!review.getSender().equals(user)) {
            log.info("Отзыв {} не удален, так как удаляющий не автор отзыва", review.getId());
            return project;
        }

        if (project.getReviews().contains(review)) {
            project.getReviews().remove(review);
            log.info("Удаление отзыва {} из проекта с id {}", review.getId(), projectId);
            return saveProject(project);
        }

        log.info("Отзыв {} не найден в проекте {} при попытке удаления", review.getId(), projectId);
        return project;
    }

}

