package tictactoe.domain.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
public class Game {

    private final UUID gameId;
    private final GameState gameState;

    public Game(GameState gameState){
        this.gameId = UUID.randomUUID();
        this.gameState = gameState;
    }

    public Game(UUID gameId, GameState gameState) {
        this.gameId = gameId;
        this.gameState = gameState;
    }
}
