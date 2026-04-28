package tictactoe.web.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateGameRequest {
    private boolean vsComputer;
}
