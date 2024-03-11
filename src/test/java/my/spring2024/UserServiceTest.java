package my.spring2024;

import my.spring2024.app.UserService;
import my.spring2024.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setFirstname("Ivan");
        user.setFirstname("Ivanov");
        User returnedUser = userService.saveUser(user);
        assertNotEquals(0, returnedUser.getId());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setFirstname("Ivan");
        user.setFirstname("Ivanov");
        userService.saveUser(user);
        User returnedUser = userService.getUserById(user.getId());
        assertEquals(user.getId(), returnedUser.getId());
        assertEquals(user.getFirstname(), returnedUser.getFirstname());
    }
}
