package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.*;
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
    private final ReviewService reviewService;
    public UserService(UserRepository userRepository, ReviewService reviewService) {
        this.userRepository = userRepository;
        this.reviewService = reviewService;
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
     * Добавляет отзыв к отправителю и получателю.
     *
     * @param senderId Идентификатор отправителя отзыва.
     * @param receiverId Идентификатор получателя отзыва.
     * @param review Отзыв, который нужно добавить.
     */
    public void addReviewToUsers(Long senderId, Long receiverId, Review review) {
        User sender = getUserById(senderId);
        User receiver = getUserById(receiverId);
        if (sender == null || receiver == null) {
            log.info("Не удалось добавить отзыв {} к пользователям: пользователь с id {} не найден", review.getId(), sender == null ? senderId : receiverId);
            return;
        }

        sender.getSentReviews().add(review);
        receiver.getReceivedReviews().add(review);
        reviewService.addReceiverToReview(receiver, review);
        reviewService.saveReview(review);
        log.info("Добавление отзыва {} к отправителю с id {} и получателю с id {}", review.getId(), senderId, receiverId);
        saveUser(sender);
        saveUser(receiver);
    }

    /**
     * Удаляет отзыв у отправителя и получателя.
     *
     * @param senderId Идентификатор отправителя отзыва.
     * @param receiverId Идентификатор получателя отзыва.
     * @param review Отзыв, который нужно удалить.
     */
    public void removeReviewFromUsers(Long senderId, Long receiverId, Review review) {
        User sender = getUserById(senderId);
        User receiver = getUserById(receiverId);
        if (sender == null || receiver == null) {
            log.info("Не удалось удалить отзыв {}: пользователь с id {} не найден", review.getId(), sender == null ? senderId : receiverId);
            return;
        }

        if (!sender.getSentReviews().contains(review) || !receiver.getReceivedReviews().contains(review)) {
            log.info("Отзыв {} не найден у пользователя с id {} при попытке удаления"
                    , review.getId(), !sender.getSentReviews().contains(review) ? senderId : receiverId);
            return;
        }

        sender.getSentReviews().remove(review);
        receiver.getReceivedReviews().remove(review);
        reviewService.deleteReview(review.getId());
        log.info("Удаление отзыва {} у отправителя с id {} и получателя с id {}", review.getId(), senderId, receiverId);
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

    /**
     * Дает пользователю права администратора
     * @param userId идентификатор пользователя
     */
    public void setAdminRole(long userId) {
        User user = getUserById(userId);
        if (user == null)
            return;

        if (user.getRole() == Role.ADMIN) {
            log.info("Пользователь с id {} уже имеет роль администратора.", userId);
            return;
        }

        user.setRole(Role.ADMIN);
        userRepository.save(user);
        log.info("Пользователь с id {} получил права администратора", userId);
        }

    /**
     * Отнимает у пользователя права администратора.
     * @param userId идентификатор пользователя
     */
    public void removeAdminRole(long userId) {
        User user = getUserById(userId);
        if (user == null)
            return;

        if (user.getRole() != Role.ADMIN) {
            log.info("Пользователь с id {} не имеет роли администратора", userId);
            return;
        }

        user.setRole(Role.USER);
        userRepository.save(user);
        log.info("У пользователя с id {} отнята роль администратора", userId);
    }
}