package tictactoe.domain.service;

import org.springframework.stereotype.Service;

import tictactoe.domain.model.GameState;

@Service
public interface GameLogicService {
    int[] getNextMove(GameState gameState);

    boolean isGameOver(GameState gameState);

    boolean validateGameField(GameState oldstate, GameState newState);

    Integer getWinnerMark(GameState gameState);

    boolean isBoardFull(GameState gameState);
}
