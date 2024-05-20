package my.spring2024.api;

import jakarta.validation.Valid;
import my.spring2024.api.DTO.UserDTO;
import my.spring2024.app.UserService;
import my.spring2024.domain.User;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Контроллер для управления пользователями.
 * Предоставляет методы для создания, получения, удаления пользователей.
 */
@RestController
@RequestMapping("/api/users")
public class  UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * Создает нового пользователя.
     *
     * @param userDTO dto пользователя для сохранения
     * @return сохраненный dto пользователя
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        User savedUser = userService.saveUser(convertToEntity(userDTO));
        return ResponseEntity.ok(convertToDto(savedUser));
    }

    /**
     * Получает dto пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return dto пользователя, если найден, или Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(user.get()));
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя для удаления
     * @return No Content, если пользователь успешно удален,
     * Или not found если такого пользователя не существует
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id) == null){
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
    private UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}