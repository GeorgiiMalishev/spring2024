package my.spring2024;

import my.spring2024.app.ProjectService;
import my.spring2024.app.UserService;
import my.spring2024.app.ReviewService;
import my.spring2024.domain.Project;
import my.spring2024.domain.Review;
import my.spring2024.domain.TeamRoleTag;
import my.spring2024.domain.User;
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
        User returnedUser = userService.getUserById(user.getId());
        assertEquals(user.getId(), returnedUser.getId());
    }

    @Test
    public void testDeleteUser(){
        User user = new User();
        var id = userService.saveUser(user).getId();
        userService.deleteUser(id);
        assertNull(userService.getUserById(id));
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
    public void testGetUsersByRole() {
        TeamRoleTag role = TeamRoleTag.DEVELOPER;
        userService.saveUser(User.builder().role(role).build());
        List<User> users = userService.getUsersByRole(role);
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
        projectService.removeUserFromProject(project.getId(), user);
        List<User> users = userService.getUsersByPastProject(project.getId());
        assertEquals(1, users.size());
    }

    @Test
    public void testAddReviewToUser() {
        var userId = userService.saveUser(new User()).getId();
        Review review = reviewService.saveReview(new Review());
        User updatedUser = userService.addReviewToUser(userId, review);
        assertNotNull(updatedUser);
    }

    @Test
    public void testRemoveReviewFromUser() {
        var userId = userService.saveUser(new User()).getId();
        Review review = reviewService.saveReview(new Review());
        userService.addReviewToUser(userId, review);
        User updatedUser = userService.removeReviewFromUser(userId, review);
        assertNotNull(updatedUser);
    }
}
