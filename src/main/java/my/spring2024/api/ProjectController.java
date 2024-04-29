package my.spring2024.api;

import jakarta.validation.Valid;
import my.spring2024.api.DTO.ProjectDTO;
import my.spring2024.app.ProjectService;
import my.spring2024.domain.Project;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public ProjectController(ProjectService projectService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    /**
     * Создает новый проект.
     *
     * @param projectDTO DTO проекта для сохранения
     * @return сохраненный DTO проекта
     */
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        Project savedProject = projectService.saveProject(convertToEntity(projectDTO));
        return ResponseEntity.ok(convertToDto(savedProject));
    }

    /**
     * Получает проект по его идентификатору.
     *
     * @param id идентификатор проекта
     * @return DTO проекта, если найден, или 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(project));
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

    private Project convertToEntity(ProjectDTO projectDTO) {
        return modelMapper.map(projectDTO, Project.class);
    }

    private ProjectDTO convertToDto(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }
}
