package my.spring2024.api;

import jakarta.validation.Valid;
import my.spring2024.api.DTO.ProjectDTO;
import my.spring2024.app.ProjectService;
import my.spring2024.domain.Project;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        Optional<Project> projectOptional = projectService.getProjectById(id);
        return projectOptional.map(project -> ResponseEntity.ok(convertToDto(project))).orElseGet(() -> ResponseEntity.notFound().build());
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
        if (projectService.getProjectById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    /**
     Возвращает все посты с возможностью пагинации и фильтрации.
     * @param spec спецификация для фильтрации
     * @param pageable объект для пагинации
     * @return страница постов
     */
    @GetMapping
    public ResponseEntity<Page<Project>> getAllProjects(Specification<Project> spec, Pageable pageable) {
        Page<Project> projects = projectService.getAllProjects(spec, pageable);
        return ResponseEntity.ok(projects);
    }
    private Project convertToEntity(ProjectDTO projectDTO) {
        return modelMapper.map(projectDTO, Project.class);
    }

    private ProjectDTO convertToDto(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }
}
