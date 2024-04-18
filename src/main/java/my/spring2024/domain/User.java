package my.spring2024.domain;
import jakarta.persistence.*;
import lombok.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс User представляет сущность пользователя.
 * Содержит основные атрибуты пользователя: идентификатор, имя, электронную почту, список отзывов.
 *
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя.
     */
    private String firstname;

    /**
     * Фамилия пользователя.
     */
    private String lastname;

    /**
     * Электронная почта пользователя.
     */
    private String email;

    /**
     * Ссылка на профиль пользователя на GitHub.
     */
    private URL gitHubLink;

    /**
     * Роль в команде
     */
    @Enumerated
    private TeamRoleTag role;
    /**
     * Список постов, созданных пользователем.
     */
    @OneToMany
    private List<Post> posts = new ArrayList<Post>();

    /**
     * Список отзывов, оставленных пользователем.
     */
    @OneToMany(mappedBy = "sender")
    private List<Review> sentReviews = new ArrayList<Review>();

    /**
     * Список отзывов, оставленных на пользователя.
     */
    @OneToMany(mappedBy = "receiver")
    private List<Review> receivedReviews = new ArrayList<Review>();

    /**
     * Список проектов, в которых состоит пользователь
     */
    @ManyToMany
    private List<Project> currentProjects = new ArrayList<Project>();

    /**
     * Список проектов, в которых участвовал ранее пользователь
     */
    @ManyToMany
    private List<Project> pastProjects = new ArrayList<Project>();;

    /**
     * Конструктор класса User.
     * Инициализирует объект User с указанными значениями идентификатора.
     *
     * @param id     Идентификатор пользователя.
     */
    public User(Long id) {
        this.id = id;
    }
}