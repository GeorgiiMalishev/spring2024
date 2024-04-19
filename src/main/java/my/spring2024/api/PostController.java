package my.spring2024.api;

import jakarta.validation.Valid;
import my.spring2024.api.DTO.PostDTO;
import my.spring2024.app.PostService;
import my.spring2024.domain.Post;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления постами.
 * Предоставляет методы для создания, получения и удаления постов.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final ModelMapper modelMapper;

    public PostController(PostService postService, ModelMapper modelMapper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    /**
     * Создает новый пост.
     *
     * @param postDTO dto поста для сохранения
     * @return сохраненный dto поста
     */
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) {
        Post savedPost = postService.savePost(convertToEntity(postDTO));
        return ResponseEntity.ok(convertToDto(savedPost));
    }

    /**
     * Получает пост по его идентификатору.
     *
     * @param id идентификатор поста
     * @return dto поста, если найден, или 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(post));
    }

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param id идентификатор поста для удаления
     * @return No Content, если пост успешно удален,
     * Или not found если такого поста не существует
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (postService.getPostById(id) == null){
            return ResponseEntity.notFound().build();
        }
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    private Post convertToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }
    private PostDTO convertToDto(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }
}
