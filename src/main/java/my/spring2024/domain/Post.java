package my.spring2024.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс Post представляет пост в системе.
 * Содержит основные атрибуты поста: идентификатор, текст поста, теги ролей команды, теги стеков или языков программирования, список откликнувшихся.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    /**
     * Идентификатор поста.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Текст поста.
     */
    private String text;

    /**
     * Теги ролей команды.
     */
    @ElementCollection(targetClass = TeamRoleTag.class)
    @Enumerated(EnumType.STRING)
    private List<TeamRoleTag> teamRoleTags;

    /**
     * Список пользователей, откликнувшихся на пост.
     */
    @ManyToMany
    private List<User> respondents;
}
