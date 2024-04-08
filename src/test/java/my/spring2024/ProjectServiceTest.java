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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"/create_project_schema.sql", "/insert_project_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;

    @Test
    public void testCreateProject() {
        Project project = new Project();
        Project returnedProject = projectService.saveProject(project);
        assertNotNull(returnedProject.getId());
    }

    @Test
    public void testGetProjectById() {
        Project project = new Project();
        projectService.saveProject(project);
        Project returnedProject = projectService.getProjectById(project.getId());
        assertEquals(project.getId(), returnedProject.getId());
    }

    @Test
    public void testDeleteProject(){
        Project project = new Project();
        var id = projectService.saveProject(project).getId();
        projectService.deleteProject(id);
        assertNull(projectService.getProjectById(id));
    }

    @Test
    public void testAddUserToProject() {
        Project project = projectService.saveProject(new Project());
        User user = userService.saveUser(new User());
        Project updatedProject = projectService.addUserToProject(project.getId(), user);
        assertTrue(updatedProject.getUsers().contains(user));
    }

    @Test
    public void testRemoveUserFromProject() {
        Project project = projectService.saveProject(new Project());
        User user = userService.saveUser(new User());
        projectService.addUserToProject(project.getId(), user);
        Project updatedProject = projectService.removeUserFromProject(project.getId(), user);
        assertFalse(updatedProject.getUsers().contains(user));
    }

    @Test
    public void testAddReviewToProject() {
        Project project = projectService.saveProject(new Project());
        Review review = reviewService.saveReview(new Review());
        Project updatedProject = projectService.addReviewToProject(project.getId(), review);
        assertTrue(updatedProject.getReviews().contains(review));
    }

    @Test
    public void testRemoveReviewFromProject() {
        Project project = projectService.saveProject(new Project());
        Review review = reviewService.saveReview(new Review());
        projectService.addReviewToProject(project.getId(), review);
        Project updatedProject = projectService.removeReviewFromProject(project.getId(), review);
        assertFalse(updatedProject.getReviews().contains(review));
    }
}
