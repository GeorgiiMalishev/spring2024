package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.Post;
import my.spring2024.domain.Review;
import my.spring2024.domain.TeamRoleTag;
import my.spring2024.domain.User;
import my.spring2024.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления пользователями в приложении.
 */
@Slf4j
@Service
@Component
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Сохраняет пользователя в базе данных.
     * @return сохраненный пользователь
     */
    public User saveUser(User user){
        log.info("Сохранен пользователь {}", user.getId());
        return userRepository.save(user);
    }

    /**
     * Возвращает пользователя с заданым идентификатором
     * @param id Идентификатор пользователя
     * @return пользователя; если пользователь не найден, то null
     */
    public User getUserById(long id) {
        User user = userRepository.findById(id);
        if (user == null)
            log.info("Не удалось найти пользователя с id {}", id);
        else {
            log.info("Пользователь с id {} найден", id);
            return user;
        }
        return null;
    }
}