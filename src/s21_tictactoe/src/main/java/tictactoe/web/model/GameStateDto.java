package tictactoe.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStateDto {
    private GameFieldDto gameFieldDto;
    private boolean playersTurn;
    private int lastPlayer;
    private int lastTurnX;
    private int lastTurnY;

    public GameStateDto() {}
    public GameStateDto(GameFieldDto gameFieldDto) {
        this.gameFieldDto = gameFieldDto;
    }
}

