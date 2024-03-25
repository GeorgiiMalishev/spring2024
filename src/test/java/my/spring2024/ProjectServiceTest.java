package my.spring2024;

import my.spring2024.app.ProjectService;
import my.spring2024.app.ProjectService;
import my.spring2024.domain.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"/create_project_schema.sql", "/insert_project_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

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
}
