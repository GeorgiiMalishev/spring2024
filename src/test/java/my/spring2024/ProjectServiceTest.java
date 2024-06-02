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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"/insert_project_data.sql"})
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
        Project returnedProject = projectService.getProjectById(project.getId()).get();
        assertEquals(project.getId(), returnedProject.getId());
    }

    @Test
    public void testDeleteProject(){
        Project project = new Project();
        var id = projectService.saveProject(project).getId();
        projectService.deleteProject(id);
        assertTrue(projectService.getProjectById(id).isEmpty());
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
        Project project = projectService.saveProject(Project.builder().leader(new User()).users(new ArrayList<>()).build());
        User user = userService.saveUser(new User());
        projectService.addUserToProject(project.getId(), user);
        Project updatedProject = projectService.removeUserFromProject(project.getId(), user, user);
        assertFalse(updatedProject.getUsers().contains(user));
    }

    @Test
    public void testAddReviewToProject() {
        Project project = projectService.saveProject(new Project());
        User sender = userService.saveUser(new User());
        Review review = reviewService.saveReview(Review.builder().rating(5).build());
        projectService.addReviewToProject(sender.getId(), project.getId(), review);
        assertTrue(projectService.getProjectById(project.getId()).get().getReviews().contains(review));
    }

    @Test
    public void testRemoveReviewFromProject() {
        Project project = projectService.saveProject(new Project());
        User user = userService.saveUser(new User());
        Review review = reviewService.saveReview(Review.builder().rating(5).build());
        projectService.addReviewToProject(user.getId(), project.getId(), review);
        projectService.removeReviewFromProject(user.getId(), project.getId(), review);
        assertFalse(projectService.getProjectById(project.getId()).get().getReviews().contains(review));
    }

    @Test
    public void testGetAllProjects(){
        assertEquals(2, projectService.getAllProjects(null, Pageable.unpaged()).getTotalElements());
    }

    @Test
    public void searchProjectsByKeyword_shouldThrowExceptionForEmptyKeyword() {
        String keyword = null;
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(IllegalArgumentException.class, () -> projectService.searchProjectsByKeyword(keyword, pageable));
    }

    @Test
    public void searchProjectsByDescriptionKeyword() {
        String keyword = "for";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> result = projectService.searchProjectsByKeyword(keyword, pageable);
        assertEquals(2, result.getTotalElements());
    }
    @Test
    public void searchProjectsByNameKeyword2() {
        String keyword = "Project 1";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> result = projectService.searchProjectsByKeyword(keyword, pageable);
        assertEquals(1, result.getTotalElements());
    }
}
