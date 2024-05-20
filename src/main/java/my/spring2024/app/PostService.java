package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Post;
import my.spring2024.domain.User;
import my.spring2024.infrastructure.PostRepository;
import my.spring2024.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления постами в приложении.
 */
@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
        if (post.isEmpty()) {
            log.warn("Не удалось найти пост с id {}", id);
            return null;
        } else {
            log.info("Пост с id {} найден", id);
            return post.get();
        }
    }

    /**
     * Возвращает все посты.
     * @return список всех постов.
     */
    public List<Post> getAllPosts() {
        var posts = postRepository.findAll();
        log.info("Найдено {} постов", posts.size());
        return posts;
    }

    /**
     * Обновляет текст поста.
     * @param id Идентификатор поста.
     * @param newText Новый текст поста.
     * @return обновленный пост; если пост не найден, то null.
     */
    public Post updatePostText(Long id, String newText) {
        var post = postRepository.findById(id);
        if (post.isEmpty()) {
            log.warn("Не удалось найти пост с id {}", id);
            return null;
        }

        Post existingPost = post.get();
        existingPost.setText(newText);
        postRepository.save(existingPost);
        log.info("Обновлен текст поста с id {}", id);
        return existingPost;
    }

    /**
     * Удаляет пост из базы данных по идентификатору.
     * @param id Идентификатор поста.
     */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
        log.info("Удален пост с id {}", id);
    }

    /**
     * Добавляет респондента к посту.
     * @param postId Идентификатор поста.
     * @param userId Идентификатор пользователя.
     * @return обновленный пост; если пост или пользователь не найдены, то null.
     */
    public Post addRespondentToPost(Long postId, Long userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isEmpty() || userOptional.isEmpty()) {
            log.info("Не удалось добавить респондента к посту: пост с id {} или пользователь с id {} не найден", postId, userId);
            return null;
        }

        Post post = postOptional.get();
        User user = userOptional.get();

        post.getRespondents().add(user);
        postRepository.save(post);
        log.info("Пользователь с id {} добавлен как респондент к посту с id {}", userId, postId);
        return post;
    }

    /**
     * Удаляет респондента из поста.
     * @param postId Идентификатор поста.
     * @param userId Идентификатор пользователя.
     * @return обновленный пост; если пост или пользователь не найдены, то null.
     */
    public Post removeRespondentFromPost(Long postId, Long userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isEmpty() || userOptional.isEmpty()) {
            log.info("Не удалось удалить респондента из поста: пост с id {} или пользователь с id {} не найден", postId, userId);
            return null;
        }

        Post post = postOptional.get();
        User user = userOptional.get();

        if (!post.getRespondents().remove(user)) {
            log.info("Пользователь с id {} не найден среди респондентов поста с id {}", userId, postId);
            return null;
        }

        postRepository.save(post);
        log.info("Пользователь с id {} удален из респондентов поста с id {}", userId, postId);
        return post;
    }
}
