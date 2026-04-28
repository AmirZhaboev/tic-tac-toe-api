package tictactoe.domain.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class GameField {
    public static final int EMPTY = 0;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;

    private int[][] gameField = new int[3][3];
}
