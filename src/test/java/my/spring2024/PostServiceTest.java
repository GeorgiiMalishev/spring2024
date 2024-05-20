package my.spring2024;

import my.spring2024.app.PostService;
import my.spring2024.app.UserService;
import my.spring2024.domain.Post;
import my.spring2024.domain.User;
import my.spring2024.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"/create_post_schema.sql", "/insert_post_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        user1 = new User();
        userService.saveUser(user1);

        user2 = new User();
        userService.saveUser(user2);
    }

    @Test
    public void testCreatePost() {
        Post post = new Post();
        Post returnedPost = postService.savePost(post);
        assertNotNull(returnedPost.getId());
    }

    @Test
    public void testGetPostById() {
        Post post = new Post();
        postService.savePost(post);
        Post returnedPost = postService.getPostById(post.getId());
        assertEquals(post.getId(), returnedPost.getId());
    }

    @Test
    public void testDeletePost(){
        Post post = new Post();
        var id = postService.savePost(post).getId();
        postService.deletePost(id);
        assertNull(postService.getPostById(id));
    }

    @Test
    public void testGetAllPosts() {
        Post post1 = new Post();
        postService.savePost(post1);
        Post post2 = new Post();
        postService.savePost(post2);
        List<Post> posts = postService.getAllPosts();
        assertTrue(posts.size() >= 2);
    }

    @Test
    public void testUpdatePostText() {
        Post post = new Post();
        postService.savePost(post);
        Long postId = post.getId();
        String newText = "Updated text";
        postService.updatePostText(postId, newText);
        Post updatedPost = postService.getPostById(postId);
        assertEquals(newText, updatedPost.getText());
    }

    @Test
    public void testAddRespondentToPost() {
        Post post = new Post();
        postService.savePost(post);
        Long postId = post.getId();
        postService.addRespondentToPost(postId, user1.getId());
        Post updatedPost = postService.getPostById(postId);
        assertTrue(updatedPost.getRespondents().contains(user1));
    }

    @Test
    public void testRemoveRespondentFromPost() {
        Post post = new Post();
        postService.savePost(post);
        Long postId = post.getId();
        postService.addRespondentToPost(postId, user1.getId());
        postService.removeRespondentFromPost(postId, user1.getId());
        Post updatedPost = postService.getPostById(postId);
        assertFalse(updatedPost.getRespondents().contains(user1));
    }
}