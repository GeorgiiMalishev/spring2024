package my.spring2024.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Класс Review представляет отзыв пользователя о другом пользователе.
 * Содержит информацию о оценке, тексте отзыва, отправителя и получателя.
 */
@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    /**
     * Идентификатор отзыва
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Оценка от 1 до 5
     */
    private int rating;

    /**
     * Текст отзыва
     */
    private String text;

    /**
     * Отправитель отзыва
     */
    @ManyToOne
    private User sender;

    /**
     * Получатель отзыва
     */
    @ManyToOne
    private User receiver;

    private boolean isValidRating(int rating){
        return rating >= 1 && rating <=5;
    }
}
