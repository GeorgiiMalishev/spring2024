package my.spring2024.api.DTO;

import jakarta.validation.constraints.*;
import my.spring2024.domain.Project;
import my.spring2024.domain.User;

public class ReviewDTO {
    private Long id;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @Size(max = 500, message = "Text must be no more 500 characters")
    private String text;

    @NotNull(message = "Sender is required")
    private User sender;

    private User receiver;
    private Project project;
}
