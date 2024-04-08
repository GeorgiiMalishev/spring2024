package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Project;
import my.spring2024.domain.Review;
import my.spring2024.domain.TeamRoleTag;
import my.spring2024.domain.User;
import my.spring2024.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления пользователями в приложении.
 */
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Сохраняет пользователя в базе данных.
     * @return сохраненный пользователь
     */
    public User saveUser(User user){
        var savedUser = userRepository.save(user);
        log.info("Сохранен пользователь {}", user.getId());
        return savedUser;
    }

    /**
     * Возвращает пользователя с заданым идентификатором
     * @param id Идентификатор пользователя
     * @return пользователя; если пользователь не найден, то null
     */
    public User getUserById(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()){
            log.info("Не удалось найти пользователя с id {}", id);
            return null;
        } else {
            log.info("Пользователь с id {} найден", id);
            return user.get();
        }

    }

    /**
     * Удаляет пользователя из базы данных по идентификатору.
     * @param id Идентификатор пользователя.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("Удален пользователь с id {}", id);
    }

    /**
     * Ищет пользователей по email
     * @param email email пользователя.
     * @return Пользователь, с соответствующим email.
     */
    public User getUserByEmail(String email) {
        var user = userRepository.findByEmail(email);
        if (user != null){
            log.info("Найден пользователь с email {}", email);
            return user;
        } else{
            log.info("Не удалось найти пользователя с email {}", email);
            return null;
        }
    }

    /**
     * Возвращает список пользователей с заданной ролью в команде.
     * @param role Роль в команде.
     * @return Список пользователей с заданной ролью.
     */
    public List<User> getUsersByRole(TeamRoleTag role) {
        var users = userRepository.findAllByRole(role);
        log.info("Найдено {} пользователей с ролью {}", users.size(), role);
        return users;
    }

    /**
     * Возвращает список пользователей, на данный момент состоящих в проекте.
     * @param projectId Идентификатор проекта.
     * @return Список пользователей.
     */
    public List<User> getUsersByCurrentProject(Long projectId) {
        var users = userRepository.findAllByCurrentProjects_Id(projectId);
        log.info("Найдено {} пользователей, участвующих в проекте с id {}", users.size(), projectId);
        return users;
    }

    /**
     * Возвращает список пользователей, участвовавших в проекте.
     * @param projectId Идентификатор проекта.
     * @return Список пользователей.
     */
    public List<User> getUsersByPastProject(Long projectId) {
        var users = userRepository.findAllByPastProjects_Id(projectId);
        log.info("Найдено {} пользователей, участвовавших в проекте с id {}", users.size(), projectId);
        return users;
    }


    /**
     * Добавляет отзыв к пользователю.
     *
     * @param userId Идентификатор пользователя.
     * @param review Отзыв, который нужно добавить.
     * @return Обновленный пользователь с добавленным отзывом.
     */
    public User addReviewToUser(Long userId, Review review) {
        User user = getUserById(userId);
        if (user == null) {
            log.info("Не удалось добавить отзыв {} к пользователю с id {}: пользователь не найден", review.getId(), userId);
            return null;
        }

        user.getReviews().add(review);
        log.info("Добавление отзыва {} к пользователю с id {}", review.getId(), userId);
        return saveUser(user);
    }

    /**
     * Удаляет отзыв пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param review Отзыв, который нужно удалить.
     * @return Обновленный пользователь без удаленного отзыва.
     */
    public User removeReviewFromUser(Long userId, Review review) {
        User user = getUserById(userId);
        if (user == null) {
            log.info("Не удалось удалить отзыв {} пользователя с id {}: пользователь не найден", review.getId(), userId);
            return null;
        }

        if (!user.getReviews().contains(review)) {
            log.info("Отзыв {} не найден у пользователя с id {} при попытке удаления", review.getId(), userId);
            return null;
        }

        user.getReviews().remove(review);
        log.info("Удаление отзыва {} пользователя с id {}", review.getId(), userId);
        return saveUser(user);
    }

    /**
     * Перемещает проект из текущих в прошлые проекты пользователя.
     *
     * @param user    Пользователь, для которого нужно обновить проекты.
     * @param project Проект, который нужно переместить.
     */
    public void moveProjectToPast(User user, Project project) {
        user.getCurrentProjects().remove(project);
        user.getPastProjects().add(project);
        saveUser(user);
    }
}
