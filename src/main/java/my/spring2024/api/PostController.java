package my.spring2024.api;

import my.spring2024.app.PostService;
import my.spring2024.domain.Post;
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

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Создает новый пост.
     *
     * @param post объект поста для сохранения
     * @return сохраненный объект поста
     */
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post savedPost = postService.savePost(post);
        return ResponseEntity.ok(savedPost);
    }

    /**
     * Получает пост по его идентификатору.
     *
     * @param id идентификатор поста
     * @return объект поста, если найден, или 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
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
}
