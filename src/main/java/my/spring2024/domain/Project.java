package my.spring2024.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Project представляет проект в системе.
 * Содержит основные атрибуты проекта: идентификатор, список пользователей, список отзывов на проект.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    /**
     * Идентификатор проекта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Название проекта
     */
    private String name;
    /**
     * Описание проекта
     */
    private String description;
    /**
     * Ссылка на репозиторий с проектом.
     */
    private URL link;
    /**
     * Список пользователей, участвующих в проекте.
     */
    @ManyToMany
    private List<User> users = new ArrayList<User>();
    /**
     * Лидер проекта
     */
    @ManyToOne
    private User leader;
    /**
     * Список отзывов на проект.
     */
    @OneToMany
    private List<Review> reviews = new ArrayList<Review>();

}
