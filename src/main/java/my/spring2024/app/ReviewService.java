package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Review;
import my.spring2024.domain.User;
import my.spring2024.infrastructure.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
            log.error("Некорректная оценка отзыва: {}", review.getRating());
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
}
