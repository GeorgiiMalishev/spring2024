package my.spring2024.infrastructure;

import my.spring2024.domain.Post;
import my.spring2024.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    public Page<Post> findByTitleContainingOrTextContaining(String keyable, String keyable2, Pageable pageable);
    public Page<Post> findByAuthor(User author, Pageable pageable);
}
