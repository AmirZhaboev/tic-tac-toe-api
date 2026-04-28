package tictactoe.domain.service;

import org.springframework.stereotype.Service;
import tictactoe.domain.model.GameField;

@Service
public class GameFieldServiceImpl implements GameFieldService {
    @Override
    public boolean isCellEmpty(GameField gameField, int x, int y) {
        return gameField.getGameField()[x][y] == GameField.EMPTY;
    }

    @Override
    public void fillCell(GameField gameField, int x, int y, int player) {
        if (isCellEmpty(gameField, x, y)) {
            gameField.getGameField()[x][y] = player;
        } else {
            throw new IllegalStateException("Клетка уже занята");
        }
    }

    @Override
    public void reset(GameField field) {
        int[][] f = field.getGameField();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                f[i][j] = GameField.EMPTY;
            }
        }
    }

}
