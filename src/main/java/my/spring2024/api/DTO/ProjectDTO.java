package my.spring2024.api.DTO;

import my.spring2024.domain.Review;
import my.spring2024.domain.User;
import jakarta.validation.constraints.*;

import java.net.URL;
import java.util.List;

public class ProjectDTO {
    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 2000, message = "Description must be no more 2000 characters")
    private String description;

    private URL link;

    @NotNull(message = "At least one user is required")
    private List<User> users;

    @NotNull(message = "Leader is required")
    private User leader;

    private List<Review> reviews;

}
