package my.spring2024.api.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import my.spring2024.domain.TeamRoleTag;
import my.spring2024.domain.User;

import java.util.List;

public class PostDTO {
    private Long id;

    @NotNull(message = "Text is required")
    @Size(min = 10, max = 2000, message = "Text must be between 10 and 2000 characters")
    private String text;

    @NotEmpty(message = "At least one team role tag is required")
    private List<TeamRoleTag> teamRoleTags;

    private List<User> respondents;
}
