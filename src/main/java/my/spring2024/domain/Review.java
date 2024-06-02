package my.spring2024.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс Review представляет отзыв пользователя о другом пользователе.
 * Содержит информацию об оценке, тексте отзыва, отправителя и получателя.
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

    /**
     * Проект с отзывом
     */
    @ManyToOne
    private Project project;

    /**
     * Устанавливает рейтинг.
     *
     * @param rating рейтинг для установки (должен быть в пределах от 1 до 5 включительно)
     * @throws IllegalArgumentException если рейтинг не находится в допустимых пределах
     */
    public void setRating(int rating) {
        if (rating <= 0 || rating > 5) throw new IllegalArgumentException();
        this.rating = rating;
    }

    /**
     * Устанавливает текст.
     *
     * @param text текст для установки
     * @throws IllegalArgumentException если текст равен null
     */
    public void setText(String text) {
        if (text == null) throw new IllegalArgumentException();
        this.text = text;
    }

    /**
     * Устанавливает отправителя.
     *
     * @param sender отправитель для установки
     * @throws IllegalArgumentException если отправитель равен null
     */
    public void setSender(User sender) {
        if (sender == null) throw new IllegalArgumentException();
        this.sender = sender;
    }

    /**
     * Устанавливает получателя.
     *
     * @param receiver получатель для установки
     * @throws IllegalArgumentException если получатель равен null
     */
    public void setReceiver(User receiver) {
        if (receiver == null) throw new IllegalArgumentException();
        this.receiver = receiver;
    }

    /**
     * Устанавливает проект.
     *
     * @param project проект для установки
     * @throws IllegalArgumentException если проект равен null
     */
    public void setProject(Project project) {
        if (project == null) throw new IllegalArgumentException();
        this.project = project;
    }

    public boolean isValidRating(int rating){
        return rating >= 1 && rating <=5;
    }
}
