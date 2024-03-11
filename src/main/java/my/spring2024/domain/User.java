package my.spring2024.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class User {
    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя пользователя.
     */
    @Setter
    private String firstname;

    /**
     * Фамилия пользователя.
     */
    @Setter
    private String lastname;

    /**
     * Электронная почта пользователя.
     */
    @Setter
    private String email;

    /**
     * Ссылка на профиль пользователя на GitHub.
     */
    @Setter
    private String gitHubLink;

    /**
     * Роль в команде
     */
    @Setter
    private TeamRoleTag role;
    /**
     * Список постов, созданных пользователем.
     */
    @OneToMany
    @Setter
    private List<Post> posts;

    /**
     * Список отзывов, оставленных на пользователя.
     */
    @OneToMany
    @Setter
    private List<Review> reviews;

    /**
     * Список проектов, в которых состоит пользователь
     */
    @ManyToMany
    @Setter
    private List<Project> currentProjects;

    /**
     * Список проектов, в которых участвовал ранее пользователь
     */
    @ManyToMany
    @Setter
    private List<Project> pastProjects;

    /**
     * Конструктор класса User.
     * Инициализирует объект User с указанными значениями идентификатора.
     *
     * @param id     Идентификатор пользователя.
     */
    public User(long id) {
        this.id = id;
    }
}