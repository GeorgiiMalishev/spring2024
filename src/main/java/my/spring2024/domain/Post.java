package my.spring2024.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Post представляет пост в системе.
 * Содержит основные атрибуты поста: идентификатор, текст поста, теги ролей команды, теги стеков или языков программирования, список откликнувшихся.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    /**
     * Идентификатор поста.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Автор поста
     */
    @ManyToOne
    private User author;

    /**
     * Заголовок поста
     */
    private String title;

    /**
     * Текст поста.
     */
    private String text;

    /**
     * Теги ролей команды.
     */
    @ElementCollection(targetClass = TeamRoleTag.class)
    @Enumerated(EnumType.STRING)
    private List<TeamRoleTag> teamRoleTags = new ArrayList<TeamRoleTag>();

    /**
     * Список пользователей, откликнувшихся на пост.
     */
    @ManyToMany
    private List<User> respondents = new ArrayList<User>();

    /**
     * Устанавливает текст.
     *
     * @param text текст для установки
     * @throws IllegalArgumentException если текст равен null
     */
    public void setText(String text) {
        if(text == null) throw new IllegalArgumentException();
        this.text = text;
    }
}
