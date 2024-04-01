package my.spring2024.api;

import my.spring2024.app.ProjectService;
import my.spring2024.domain.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления пользователями.
 * Предоставляет методы для создания, получения, удаления проектов.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Создает новый проект.
     *
     * @param project объект проекта для сохранения
     * @return сохраненный объект проекта
     */
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project savedProject = projectService.saveProject(project);
        return ResponseEntity.ok(savedProject);
    }

    /**
     * Получает проект по его идентификатору.
     *
     * @param id идентификатор проекта
     * @return объект проекта, если найден, или 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project);
    }

    /**
     * Удаляет проект по его идентификатору.
     *
     * @param id идентификатор проекта для удаления
     * @return 204 No Content, если проект успешно удален,
     * Или not found если такого проекта не существует
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (projectService.getProjectById(id) == null){
            return ResponseEntity.notFound().build();
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
