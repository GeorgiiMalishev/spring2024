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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Сервис для управления проектами в приложении.
 */
@Service
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    private final ReviewService reviewService;

    public ProjectService(ProjectRepository projectRepository, UserService userService, ReviewService reviewService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.reviewService = reviewService;
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
            user.getCurrentProjects().add(project);
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
            userService.moveProjectToPast(user, project);
            project.getUsers().remove(user);
            log.info("Удаление пользователя {} из проекта с id {}", user.getId(), projectId);
            return saveProject(project);
        }

        log.info("Пользователь {} не найден в проекте {} при попытке удаления", user.getId(), projectId);
        return project;
    }

    /**
     * Добавляет отзыв к отправителю и проекту.
     *
     * @param senderId Идентификатор отправителя отзыва.
     * @param projectId Идентификатор проекта.
     * @param review Отзыв, который нужно добавить.
     */
    public void addReviewToProject(Long senderId, Long projectId, Review review) {
        Optional<User> optionalSender = userService.getUserById(senderId);
        Project project = getProjectById(projectId);
        if (optionalSender.isEmpty() || project == null) {
            log.warn("Не удалось добавить отзыв {}: сущность с id {} не найдена", review.getId(), optionalSender.isEmpty() ? senderId : projectId);
            return;
        }
        User sender = optionalSender.get();

        sender.getSentReviews().add(review);
        project.getReviews().add(review);
        reviewService.addProjectToReview(project, review);
        reviewService.saveReview(review);
        log.info("Добавление отзыва {} к отправителю с id {} и проекту с id {}", review.getId(), senderId, projectId);
        userService.saveUser(sender);
        saveProject(project);
    }

    /**
     * Удаляет отзыв у отправителя и получателя.
     *
     * @param senderId Идентификатор отправителя отзыва.
     * @param projectId Идентификатор проекта, получившго отзыв.
     * @param review Отзыв, который нужно удалить.
     */
    public void removeReviewFromProject(Long senderId, Long projectId, Review review) {
        Optional<User> optionalSender = userService.getUserById(senderId);
        Project project = getProjectById(projectId);
        if (optionalSender.isEmpty() || project == null) {
            log.warn("Не удалось удалить отзыв {}: сущность с id {} не найдена", review.getId(), optionalSender.isEmpty() ? senderId : projectId);
            return;
        }

        User sender = optionalSender.get();
        if (!sender.getSentReviews().contains(review) || !project.getReviews().contains(review)) {
            log.info("Отзыв {} не найден у сущности с id {} при попытке удаления"
                    , review.getId(), !sender.getSentReviews().contains(review) ? senderId : projectId);
            return;
        }

        project.getReviews().remove(review);
        reviewService.deleteReview(review.getId());
        log.info("Удаление отзыва {} у отправителя с id {} и проекта с id {}", review.getId(), senderId, projectId);
    }

}

