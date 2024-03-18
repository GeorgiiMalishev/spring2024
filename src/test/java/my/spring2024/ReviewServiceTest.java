package my.spring2024;

import my.spring2024.app.ReviewService;
import my.spring2024.domain.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = "/create_review_schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/insert_review_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;

    @Test
    public void testCreateReview() {
        Review review = new Review();
        Review returnedReview = reviewService.saveReview(review);
        assertNotEquals(null, returnedReview.getId());
    }

    @Test
    public void testGetReviewById() {
        Review review = new Review();
        reviewService.saveReview(review);
        Review returnedReview = reviewService.getReviewById(review.getId());
        assertEquals(review.getId(), returnedReview.getId());
    }
}
