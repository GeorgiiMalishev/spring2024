package my.spring2024;

import my.spring2024.app.PostService;
import my.spring2024.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = "/create_post_schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/insert_post_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Test
    public void testCreatePost() {
        Post post = new Post();
        Post returnedPost = postService.savePost(post);
        assertNotEquals(null, returnedPost.getId());
    }

    @Test
    public void testGetPostById() {
        Post post = new Post();
        postService.savePost(post);
        Post returnedPost = postService.getPostById(post.getId());
        assertEquals(post.getId(), returnedPost.getId());
    }
}