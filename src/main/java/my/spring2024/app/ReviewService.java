package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Review;
import my.spring2024.infrastructure.ReviewRepository;
import org.springframework.stereotype.Service;

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
     * @return сохраненный отзыв
     */
    public Review saveReview(Review review){
        log.info("Сохранен отзыв {}", review.getId());
        return reviewRepository.save(review);
    }

    /**
     * Возвращает отзыв с заданым идентификатором
     * @param id Идентификатор отзыва
     * @return отзыв; если отзыв не найден, то null
     */
    public Review getReviewById(Long id) {
        Review review = reviewRepository.findById(id);
        if (review == null) {
            log.info("Отзыв с id {} найден", id);
        } else {
            log.info("Не удалось найти отзыв с id {}", id);
        }
        return review;
    }
}
