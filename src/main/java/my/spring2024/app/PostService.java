package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Post;
import my.spring2024.infrastructure.PostRepository;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления постами в приложении.
 */
@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Сохраняет пост в базе данных.
     * @param post пост
     * @return сохраненный пост
     */
    public Post savePost(Post post) {
        var savedPost = postRepository.save(post);
        log.info("Сохранен пост {}", post.getId());
        return savedPost;
    }

    /**
     * Возвращает пост с заданным идентификатором
     * @param id Идентификатор поста
     * @return пост; если пост не найден, то null
     */
    public Post getPostById(Long id) {
        var post = postRepository.findById(id);
        if (post.isEmpty()){
            log.info("Не удалось найти пост с id {}", id);
            return null;
        } else {
            log.info("Пост с id {} найден", id);
            return post.get();
        }
    }

    /**
     * Удаляет пост из базы данных по идентификатору.
     * @param id Идентификатор отзыва.
     */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
        log.info("Удален пост с id {}", id);
    }
}