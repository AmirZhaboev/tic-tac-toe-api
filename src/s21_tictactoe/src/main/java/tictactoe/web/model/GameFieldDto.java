package tictactoe.web.model;

import lombok.Data;

@Data
public class GameFieldDto {
    private int[][] field;

    public GameFieldDto() {
    }
}
