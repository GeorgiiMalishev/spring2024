package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Post;
import my.spring2024.domain.User;
import my.spring2024.infrastructure.PostRepository;
import my.spring2024.infrastructure.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
     * Возвращает все посты с возможностью пагинации и фильтрации.
     * @param spec спецификация для фильтрации
     * @param pageable объект для пагинации
     * @return страница постов
     */
    public Page<Post> getAllPosts(Specification<Post> spec, Pageable pageable) {
        var posts = postRepository.findAll(spec, pageable);
        log.info("Найдено {} постов", posts.getTotalElements());
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
     * @throws IllegalArgumentException если пост или пользователь не найдены.
     */
    public Post addRespondentToPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Не удалось добавить респондента: пост с id " + postId + " не найден"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Не удалось добавить респондента: пользователь с id " + userId + " не найден"));

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
     * @throws IllegalArgumentException если пост или пользователь не найдены.
     */
    public Post removeRespondentFromPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Не удалось добавить респондента: пост с id " + postId + " не найден"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Не удалось добавить респондента: пользователь с id " + userId + " не найден"));

        if (!post.getRespondents().remove(user)) {
            log.info("Пользователь с id {} не найден среди респондентов поста с id {}", userId, postId);
            return null;
        }

        postRepository.save(post);
        log.info("Пользователь с id {} удален из респондентов поста с id {}", userId, postId);
        return post;
    }

    /**
     * Ищет посты по ключевым словам в тексте или заголовке.
     * @param keyword ключевое слово для поиска.
     * @param pageable объект для пагинации.
     * @return страница постов, содержащих ключевое слово.
     * @throws IllegalArgumentException если ключевое слово пустое или null
     */
    public Page<Post> searchPostsByKeyword(String keyword, Pageable pageable) {
        if(keyword == null || keyword.isEmpty()) throw new IllegalArgumentException();
        var posts = postRepository.findByTitleContainingOrTextContaining(keyword, keyword, pageable);
        log.info("Найдено {} постов, содержащие ключевое слово '{}'", posts.getTotalElements(), keyword);
        return posts;
    }

    /**
     * Возвращает все посты, созданные пользователем.
     * @param authorId Идентификатор автора.
     * @param pageable объект для пагинации.
     * @return страница постов, созданных пользователем.
     */
    public Page<Post> getPostsByAuthor(Long authorId, Pageable pageable) {
        var author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с id " + authorId + " не найден"));
        var posts = postRepository.findByAuthor(author, pageable);
        log.info("Найдено {} постов, созданных пользователем с id {}", posts.getTotalElements(), authorId);
        return posts;
    }
}
