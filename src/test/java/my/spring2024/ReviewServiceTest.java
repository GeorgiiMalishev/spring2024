package my.spring2024;

import my.spring2024.app.ReviewService;
import my.spring2024.app.UserService;
import my.spring2024.domain.Review;
import my.spring2024.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//@Sql(scripts = {"/create_review_schema.sql", "/insert_review_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;

    @Test
    public void testCreateReview() {
        Review review = new Review();
        Review returnedReview = reviewService.saveReview(review);
        assertNotNull(returnedReview.getId());
    }

    @Test
    public void testGetReviewById() {
        Review review = new Review();
        reviewService.saveReview(review);
        Review returnedReview = reviewService.getReviewById(review.getId());
        assertEquals(review.getId(), returnedReview.getId());
    }

    @Test
    public void testDeleteReview(){
        Review review = new Review();
        var id = reviewService.saveReview(review).getId();
        reviewService.deleteReview(id);
        assertNull(reviewService.getReviewById(id));
    }

    @Test
    public void testGetReviewsByReceiver(){
        User user = userService.saveUser(new User());
        for(int i = 1; i <= 5; i++){
            userService.addReviewToUser(user.getId(), Review.builder().rating(i).build());
        }
        assertEquals(5, reviewService.getReviewsByReceiver(user).size());
    }

    @Test
    public void testGetReviewsBySender(){
        Review review = new Review();
        User user = new User();
        user.getReviews().add(review);
        userService.saveUser(user);
        assertEquals(review, reviewService.getReviewsBySender(user).getFirst());
    }

    @Test
    public void testGetAverageRating(){
        User user = userService.saveUser(new User());
        for(int i = 1; i <= 5; i++){
            userService.addReviewToUser(user.getId(), Review.builder().rating(i).build());
        }
        assertEquals(3, reviewService.getAverageRating(user));
    }
}
