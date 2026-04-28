package tictactoe.web.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoveRequest {
    private int x;
    private int y;
}
