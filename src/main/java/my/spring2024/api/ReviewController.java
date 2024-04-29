package my.spring2024.api;

import jakarta.validation.Valid;
import my.spring2024.api.DTO.ReviewDTO;
import my.spring2024.app.ReviewService;
import my.spring2024.domain.Review;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public ReviewController(ReviewService reviewService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }
    /**
     * Создает новый отзыв.
     *
     * @param reviewDTO DTO отзыва для сохранения
     * @return сохраненный DTO отзыва
     */
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        Review savedReview = reviewService.saveReview(convertToEntity(reviewDTO));
        return ResponseEntity.ok(convertToDto(savedReview));
    }

    /**
     * Получает отзыв по его идентификатору.
     *
     * @param id идентификатор отзыва
     * @return DTO отзыва, если найден, или 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(review));
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

    /**
     * Обновляет отзыв по его идентификатору.
     *
     * @param id идентификатор отзыва для обновления
     * @param reviewDTO DTO с обновленными тестом и рейтингом
     * @return DTO отзыва, если найден, или 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        Review updatedReview = reviewService.updateReview(id, convertToEntity(reviewDTO));
        if (updatedReview == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(updatedReview));
    }

    private Review convertToEntity(ReviewDTO reviewDTO) {
        return modelMapper.map(reviewDTO, Review.class);
    }

    private ReviewDTO convertToDto(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }
}
