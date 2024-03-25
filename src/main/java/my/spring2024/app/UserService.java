package my.spring2024.app;

import lombok.extern.slf4j.Slf4j;
import my.spring2024.domain.User;
import my.spring2024.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления пользователями в приложении.
 */
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Сохраняет пользователя в базе данных.
     * @return сохраненный пользователь
     */
    public User saveUser(User user){
        var savedUser = userRepository.save(user);
        log.info("Сохранен пользователь {}", user.getId());
        return savedUser;
    }

    /**
     * Возвращает пользователя с заданым идентификатором
     * @param id Идентификатор пользователя
     * @return пользователя; если пользователь не найден, то null
     */
    public User getUserById(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()){
            log.info("Не удалось найти пользователя с id {}", id);
            return null;
        } else {
            log.info("Пользователь с id {} найден", id);
            return user.get();
        }

    }

    /**
     * Удаляет пользователя из базы данных по идентификатору.
     * @param id Идентификатор пользователя.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("Удален пользователь с id {}", id);
    }

    /**
     * Ищет пользователей по email
     * @param email email пользователя.
     * @return Пользователь, с соответствующим email.
     */
    public User getUserByEmail(String email) {
        var user = userRepository.findByEmail(email);
        if (user != null){
            log.info("Найден пользователь с email {}", email);
            return user;
        } else{
            log.info("Не удалось найти пользователя с email {}", email);
            return null;
        }
    }
}