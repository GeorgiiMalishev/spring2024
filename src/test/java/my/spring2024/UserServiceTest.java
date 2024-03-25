package my.spring2024;

import my.spring2024.app.UserService;
import my.spring2024.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"/create_user_schema.sql", "/insert_user_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testCreateUser() {
        User user = new User();
        User returnedUser = userService.saveUser(user);
        assertNotNull(returnedUser.getId());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        userService.saveUser(user);
        User returnedUser = userService.getUserById(user.getId());
        assertEquals(user.getId(), returnedUser.getId());
    }
}
