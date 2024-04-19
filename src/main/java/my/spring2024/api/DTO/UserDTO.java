package my.spring2024.api.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.spring2024.domain.Post;
import my.spring2024.domain.Project;
import my.spring2024.domain.Review;
import my.spring2024.domain.TeamRoleTag;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotNull(message = "Firstname is required")
    @Size(min = 2, max = 100, message = "Firstname must be between 2 and 100 characters")
    private String firstname;

    @Size(min = 2, max = 100, message = "Lastname must be between 2 and 100 characters")
    private String lastname;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String gitHubLink;

    @NotNull(message = "Role is required")
    private TeamRoleTag role;

    private List<Post> posts;
    private List<Review> sentReviews;
    private List<Review> receivedReviews;
    private List<Project> currentProjects;
    private List<Project> pastProjects;
}
