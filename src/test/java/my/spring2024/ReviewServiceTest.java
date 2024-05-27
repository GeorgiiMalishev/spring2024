package my.spring2024;

import my.spring2024.app.ProjectService;
import my.spring2024.app.ReviewService;
import my.spring2024.app.UserService;
import my.spring2024.domain.Project;
import my.spring2024.domain.Review;
import my.spring2024.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"/insert_review_data.sql"})
public class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @Test
    public void testCreateReview() {
        Review review = Review.builder().rating(5).build();
        Review returnedReview = reviewService.saveReview(review);
        assertNotNull(returnedReview.getId());
    }

    @Test
    public void testGetReviewById() {
        Review review = Review.builder().rating(5).build();
        reviewService.saveReview(review);
        Review returnedReview = reviewService.getReviewById(review.getId());
        assertEquals(review.getId(), returnedReview.getId());
    }

    @Test
    public void testDeleteReview(){
        Review review = Review.builder().rating(5).build();
        var id = reviewService.saveReview(review).getId();
        reviewService.deleteReview(id);
        assertNull(reviewService.getReviewById(id));
    }

    @Test
    public void testGetReviewsByReceiver(){
        User sender = userService.saveUser(new User());
        User receiver = userService.saveUser(new User());
        for(int i = 1; i <= 5; i++){
            userService.addReviewToUsers(sender.getId(), receiver.getId(), Review.builder().rating(i).build());
        }
        assertEquals(5, reviewService.getReviewsByReceiver(receiver).size());
    }

    @Test
    public void testGetReviewsBySender(){
        Review review = Review.builder().rating(5).build();
        User sender = userService.saveUser(new User());
        User receiver = userService.saveUser(new User());
        userService.addReviewToUsers(sender.getId(), receiver.getId(), review);
        assertEquals(review, reviewService.getReviewsBySender(sender).getFirst());
    }

    @Test
    public void testGetAverageRating(){
        User sender = userService.saveUser(new User());
        User receiver = userService.saveUser(new User());
        for(int i = 1; i <= 5; i++){
            userService.addReviewToUsers(sender.getId(), receiver.getId(), Review.builder().rating(i).build());
        }
        assertEquals(3, reviewService.getAverageRating(receiver));
    }

    @Test
    public void testUpdateReviewSuccess() {
        User user = userService.saveUser(new User());
        Review initialReview = reviewService.saveReview(new Review(null, 5, "text", user, new User(), null));
        Long reviewId = reviewService.saveReview(initialReview).getId();

        Review updatedReview = new Review(null, 4, "Updated Text", user, null, null);
        Review result = reviewService.updateReview(reviewId, updatedReview);

        assertNotNull(result);
        assertEquals(initialReview.getId(), result.getId());
        assertEquals(updatedReview.getRating(), result.getRating());
        assertEquals(updatedReview.getText(), result.getText());
    }

    @Test
    public void testUpdateReviewNotFound() {
        Review nonExistentReview = Review.builder().rating(3).build();
        Review nonExistentResult = reviewService.updateReview(999L, nonExistentReview);

        assertNull(nonExistentResult);
    }

    @Test
    public void testGetAllReviews(){
        Page<Review> reviews = reviewService.getAllReviews(null, Pageable.unpaged());
        assertEquals(2, reviews.getTotalElements());
    }

    @Test
    public void testGetReviewsByProject(){
        User sender = userService.saveUser(new User());
        Project project = projectService.saveProject(new Project());
        for(int i = 1; i <= 5; i++){
            projectService.addReviewToProject(sender.getId(), project.getId(), Review.builder().rating(i).build());
        }
        assertEquals(5, reviewService.getReviewsByProject(project).size());
    }

    @Test
    public void testAddSenderToReview(){
        User sender = userService.saveUser(new User());
        Project project = projectService.saveProject(new Project());
        Review review = reviewService.saveReview(Review.builder().rating(1).build());
        projectService.addReviewToProject(sender.getId(), project.getId(), review);
        assertEquals(sender, review.getSender());
    }

    @Test
    public void testAddReceiverToReview(){
        User sender = userService.saveUser(new User());
        User receiver = userService.saveUser(new User());
        Review review = reviewService.saveReview(Review.builder().rating(1).build());
        userService.addReviewToUsers(sender.getId(), receiver.getId(), review);
        assertEquals(receiver, review.getReceiver());
    }
}
