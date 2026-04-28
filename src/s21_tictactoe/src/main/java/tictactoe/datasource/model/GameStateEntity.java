package tictactoe.datasource.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import tictactoe.domain.model.GameStatus;

@Data
@NoArgsConstructor
@Entity
@Table(name = "games_states")
public class GameStateEntity {

    @Id
    private UUID id;

    @Embedded
    private GameFieldEntity gameFieldEntity = new GameFieldEntity();

    private boolean vsComputer;

    private UUID playerXId;
    private UUID playerOId;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    private UUID currentPlayerId;
    private UUID winnerId;

    private int lastTurnX = -1;
    private int lastTurnY = -1;

    private LocalDateTime createdAt;
}
