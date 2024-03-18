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
        log.info("Сохранен пост {}", post.getId());
        return postRepository.save(post);
    }

    /**
     * Возвращает пост с заданным идентификатором
     * @param id Идентификатор поста
     * @return пост; если пост не найден, то null
     */
    public Post getPostById(Long id) {
        Post post = postRepository.findById(id);
        if (post == null)
            log.info("Не удалось найти пост с id {}", id);
        else {
            log.info("Пост с id {} найден", id);
            return post;
        }
        return null;
    }
}