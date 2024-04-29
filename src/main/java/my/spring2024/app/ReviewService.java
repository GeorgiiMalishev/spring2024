package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Project;
import my.spring2024.domain.Review;
import my.spring2024.domain.User;
import my.spring2024.infrastructure.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления отзывами в приложении.
 */
@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Сохраняет отзыв в базе данных.
     * Проверяет валидность оценки отзыва перед сохранением.
     *
     * @param review Отзыв, который нужно сохранить.
     * @return Сохраненный отзыв, или null, если оценка отзыва невалидна.
     */
    public Review saveReview(Review review) {
        if (!review.isValidRating(review.getRating())) {
            log.error("Некорректная оценка {} отзыва: {}", review.getRating(), review.getId());
            return null;
        }
        var savedReview = reviewRepository.save(review);
        log.info("Сохранен отзыв {}", review.getId());
        return savedReview;
    }

    /**
     * Возвращает отзыв с заданым идентификатором
     * @param id Идентификатор отзыва
     * @return отзыв; если отзыв не найден, то null
     */
    public Review getReviewById(Long id) {
        var review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            log.info("Не удалось найти отзыв с id {}", id);
            return null;
        } else {
            log.info("Отзыв с id {} найден", id);
            return review.get();
        }
    }

    /**
     * Удаляет отзыв из базы данных по идентификатору.
     * @param id Идентификатор отзыва.
     */
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
        log.info("Удален отзыв с id {}", id);
    }

    /**
     * Обновляет существующий отзыв в базе данных.
     * Проверяет валидность оценки отзыва перед обновлением.
     *
     * @param id      Идентификатор отзыва, который нужно обновить.
     * @param review Обновленные данные отзыва.
     * @return Обновленный отзыв, или null, если отзыв не найден или оценка отзыва невалидна.
     */
    public Review updateReview(Long id, Review review) {
        Optional<Review> existingReview = reviewRepository.findById(id);
        if (existingReview.isPresent()) {
            Review updatedReview = existingReview.get();
            updatedReview.setRating(review.getRating());
            updatedReview.setText(review.getText());
            reviewRepository.save(updatedReview);
            log.info("Обновлен отзыв с id {}", id);
            return updatedReview;
        } else {
            log.info("Не удалось найти отзыв с id {} для обновления", id);
            return null;
        }
    }

    /**
     * Возвращает список всех отзывов, полученных конкретным пользователем.
     *
     * @param receiver Пользователь, отзывы на которого нужно получить.
     * @return Список отзывов, или пустой список, если отзывов нет.
     */
    public List<Review> getReviewsByReceiver(User receiver) {
        return reviewRepository.findAllByReceiver(receiver);
    }

    /**
     * Возвращает список всех отзывов, отправленных конкретным пользователем.
     *
     * @param sender Пользователь, отзывы которого нужно получить.
     * @return Список отзывов, или пустой список, если отзывов нет.
     */
    public List<Review> getReviewsBySender(User sender) {
        return reviewRepository.findAllBySender(sender);
    }

    /**
     * Вычисляет среднюю оценку, полученную конкретным пользователем.
     *
     * @param user Пользователь, среднюю оценку которого нужно вычислить.
     * @return Средняя оценка, или 0, если отзывов нет.
     */
    public double getAverageRating(User user) {
        List<Review> reviews = getReviewsByReceiver(user);
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);
    }

    /**
     * Добавляет отправителя к отзыву.
     *
     * @param sender   Отправитель отзыва, который должен быть добавлен к отзыву.
     * @param review   Отзыв, к которому добавляются отправитель и получатель.
     */
    public void addSenderToReview(User sender, Review review){
        if(sender.getSentReviews().contains(review)){
            review.setSender(sender);
            log.info("Добавлен отправитель с id {} к отзыву {}", sender.getId(), review.getId());
        } else {
            log.info("Не уддалось добавить отправителя с id {} к отзыву {}, отправитель не имеет этого отзыва", sender.getId(), review.getId());
        }

        reviewRepository.save(review);
    }

    /**
     * Добавляет получателя к отзыву.
     *
     * @param receiver Получатель отзыва, который должен быть добавлен к отзыву.
     * @param review   Отзыв, к которому добавляются отправитель и получатель.
     */
    public void addReceiverToReview(User receiver, Review review){
        if(receiver.getReceivedReviews().contains(review)){
            review.setReceiver(receiver);
            log.info("Добавлен получатель с id {} к отзыву {}", receiver.getId(), review.getId());
        } else {
            log.info("Не уддалось добавить получателя с id {} к отзыву {}, получатель не имеет этого отзыва", receiver.getId(), review.getId());
        }

        reviewRepository.save(review);
    }

    /**
     * Добавляет проект к отзыву.
     *
     * @param project Получатель отзыва, который должен быть добавлен к отзыву.
     * @param review   Отзыв, к которому добавляются отправитель и получатель.
     */
    public void addProjectToReview(Project project, Review review){
        if(project.getReviews().contains(review)){
            review.setProject(project);
            log.info("Добавлен проект с id {} к отзыву {}", project.getId(), review.getId());
        } else {
            log.info("Не уддалось добавить проект с id {} к отзыву {}, проект не имеет этого отзыва", project.getId(), review.getId());
        }

        reviewRepository.save(review);
    }
}
