package my.spring2024;

import my.spring2024.app.ProjectService;
import my.spring2024.app.UserService;
import my.spring2024.app.ReviewService;
import my.spring2024.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"/create_user_schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProjectService projectService;

    @Test
    public void testCreateUser() {
        User user = new User();
        User returnedUser = userService.saveUser(user);
        assertNotNull(returnedUser.getId());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        userService.saveUser(user);
        User returnedUser = userService.getUserById(user.getId()).get();
        assertEquals(user.getId(), returnedUser.getId());
    }

    @Test
    public void testDeleteUser(){
        User user = new User();
        var id = userService.saveUser(user).getId();
        userService.deleteUser(id);
        assertTrue(userService.getUserById(id).isEmpty());
    }

    @Test
    public void testGetUserByEmail(){
        var email = "example@mail.com";
        var user = User.builder().email(email).build();
        userService.saveUser(user);
        var returnedUser = userService.getUserByEmail(email);
        assertEquals(user.getId(), returnedUser.getId());
    }

    @Test
    public void testGetUsersByTeamRole() {
        TeamRoleTag role = TeamRoleTag.DEVELOPER;
        User user = new User(
                null,
                "John",
                "Doe",
                "test@example.com",
                null,
                role,
                null,
                null,
                null,
                null,
                null,
                Role.USER
        );

        userService.saveUser(user);
        List<User> users = userService.getUsersByTeamRole(role);
        assertEquals(1, users.size());
    }

    @Test
    public void testGetUsersByCurrentProject() {
        User user = userService.saveUser(new User());
        Project project = projectService.saveProject(new Project());
        projectService.addUserToProject(project.getId(), user);
        List<User> users = userService.getUsersByCurrentProject(project.getId());
        assertEquals(1, users.size());
    }

    @Test
    public void testGetUsersByPastProject() {
        User user = userService.saveUser(new User());
        Project project = projectService.saveProject(new Project());
        projectService.addUserToProject(project.getId(), user);
        projectService.removeUserFromProject(project.getId(), user, user);
        List<User> users = userService.getUsersByPastProject(project.getId());
        assertEquals(1, users.size());
    }

    @Test
    public void testAddReviewToUsers() {
        var sender = userService.saveUser(new User());
        var receiver = userService.saveUser(new User());
        Review review = reviewService.saveReview(Review.builder().rating(5).build());
        userService.addReviewToUsers(sender.getId(), receiver.getId(), review);

        Review savedReview = reviewService.getReviewById(review.getId());
        assertEquals(savedReview.getSender(), sender);
    }

    @Test
    public void testRemoveReviewFromUser() {
        var sender= userService.saveUser(new User());
        var receiver = userService.saveUser(new User());
        Review review = reviewService.saveReview(Review.builder().rating(5).build());
        userService.addReviewToUsers(sender.getId(), receiver.getId(), review);
        userService.removeReviewFromUsers(sender.getId(), receiver.getId(), review);
        assertEquals(0, reviewService.getReviewsByReceiver(receiver).size());
        assertEquals(0, reviewService.getReviewsBySender(sender).size());
    }

    @Test
    public void testSetAdminRole() {
        User user = userService.saveUser(new User());
        userService.setAdminRole(user.getId());
        User updatedUser = userService.getUserById(user.getId()).get();
        assertEquals(Role.ADMIN, updatedUser.getRole());
    }

    @Test
    public void testRemoveAdminRole() {
        User user = userService.saveUser(new User());
        user.setRole(Role.ADMIN);
        userService.saveUser(user);
        userService.removeAdminRole(user.getId());
        User updatedUser = userService.getUserById(user.getId()).get();
        assertEquals(Role.USER, updatedUser.getRole());
    }

    @Test
    public void testRemoveAdminRoleForNonAdminUser() {
        User user = userService.saveUser(new User());
        userService.removeAdminRole(user.getId());
        User updatedUser = userService.getUserById(user.getId()).get();
        assertEquals(Role.USER, updatedUser.getRole());
    }
}
