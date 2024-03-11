package my.spring2024.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Класс Project представляет проект в системе.
 * Содержит основные атрибуты проекта: идентификатор, список пользователей, список отзывов на проект.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    /**
     * Идентификатор проекта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * Название проекта
     */
    @Setter
    private String name;
    /**
     * Описание проекта
     */
    @Setter
    private String description;
    /**
     * Ссылка на репозиторий с проектом.
     */
    @Setter
    private String link;
    /**
     * Список пользователей, участвующих в проекте.
     */
    @Setter
    @ManyToMany
    private List<User> users;

    /**
     * Список отзывов на проект.
     */
    @Setter
    @OneToMany
    private List<Review> reviews;

}
