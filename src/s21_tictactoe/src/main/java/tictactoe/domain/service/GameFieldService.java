package tictactoe.domain.service;

import tictactoe.domain.model.GameField;

public interface GameFieldService {
    boolean isCellEmpty(GameField gameField, int x, int y);
    void fillCell(GameField gameField,int x, int y, int player);
    void reset(GameField field);
}
