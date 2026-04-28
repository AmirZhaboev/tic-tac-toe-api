package tictactoe.web.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import tictactoe.domain.model.GameStatus;

@Data
@NoArgsConstructor
public class GameResponse {
    private UUID gameId;
    private boolean vsComputer;
    private UUID playerXId;
    private UUID playerOId;
    private GameStatus status;
    private UUID currentPlayerId;
    private UUID winnerId;
    private int[][] board;
    private int lastTurnX;
    private int lastTurnY;
    private LocalDateTime createdAt;
}
