package tictactoe.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GameState {
    private GameField gameField;

    private boolean vsComputer;

    private UUID playerXId;
    private UUID playerOId;

    private GameStatus status;

    private UUID currentPlayerId;
    private UUID winnerId;

    private int lastTurnX;
    private int lastTurnY;

    private LocalDateTime createdAt;

    public GameState(GameField gameField) {
        this.gameField = gameField;
        this.status = GameStatus.WAITING_FOR_PLAYERS;
        this.lastTurnX = -1;
        this.lastTurnY = -1;
        this.createdAt = LocalDateTime.now();
    }
}
