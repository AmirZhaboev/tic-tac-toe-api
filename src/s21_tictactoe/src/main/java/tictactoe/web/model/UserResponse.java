package tictactoe.web.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {
    public UserResponse(UUID id, String login) {
        this.id = id;
        this.login = login;
    }

    private UUID id;
    private String login;
}
