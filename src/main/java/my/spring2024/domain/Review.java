package my.spring2024.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Класс Review представляет отзыв пользователя о другом пользователе.
 * Содержит информацию о оценке, тексте отзыва, идентификаторах отправителя и получателя.
 */
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
    private long id;

    /**
     * Оценка от 1 до 5
     */
    private int rating;

    /**
     * Текст отзыва
     */
    private String text;

    /**
     * Идентификатор отправителя отзыва
     */
    private String senderId;

    /**
     * Идентификатор получателя отзыва
     */
    private String receiverId;

    private boolean isValidRating(int rating){
        return rating >= 1 && rating <=5;
    }
}
