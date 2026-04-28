package tictactoe.datasource.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class GameFieldEntity {
    public static final int EMPTY = 0;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;

    private int cell00;
    private int cell01;
    private int cell02;

    private int cell10;
    private int cell11;
    private int cell12;

    private int cell20;
    private int cell21;
    private int cell22;
}
