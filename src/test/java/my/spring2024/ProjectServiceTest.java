package my.spring2024;

import my.spring2024.app.ProjectService;
import my.spring2024.app.ProjectService;
import my.spring2024.domain.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @Test
    public void testCreateProject() {
        Project project = new Project();
        project.setName("testProject");
        Project returnedProject = projectService.saveProject(project);
        assertNotEquals(0, returnedProject.getId());
    }

    @Test
    public void testGetProjectById() {
        Project project = new Project();
        project.setName("testProject");
        projectService.saveProject(project);
        Project returnedProject = projectService.getProjectById(project.getId());
        assertEquals(project.getId(), returnedProject.getId());
        assertEquals(project.getName(), returnedProject.getName());
    }
}
