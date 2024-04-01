package my.spring2024.api;

import my.spring2024.app.ReviewService;
import my.spring2024.domain.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления отзывами.
 * Предоставляет методы для создания, получения, удаления отзывов.
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Создает новый отзыв.
     *
     * @param review объект отзыва для сохранения
     * @return сохраненный объект отзыва
     */
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }

    /**
     * Получает отзыв по его идентификатору.
     *
     * @param id идентификатор отзыва
     * @return объект отзыва, если найден, или 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(review);
    }

    /**
     * Удаляет отзыв по его идентификатору.
     *
     * @param id идентификатор отзыва для удаления
     * @return 204 No Content, если отзыв успешно удален,
     * Или not found если такого отзыва не существует
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        if (reviewService.getReviewById(id) == null){
            return ResponseEntity.notFound().build();
        }
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
