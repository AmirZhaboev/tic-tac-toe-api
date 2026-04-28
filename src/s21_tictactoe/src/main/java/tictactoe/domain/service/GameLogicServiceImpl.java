package tictactoe.domain.service;

import org.springframework.stereotype.Service;

import tictactoe.domain.model.GameField;
import tictactoe.domain.model.GameState;

@Service
public class GameLogicServiceImpl implements GameLogicService {

    @Override
    public int[] getNextMove(GameState gameState) {
        int[][] board = gameState.getGameField().getGameField();
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == GameField.EMPTY) {
                    board[i][j] = GameField.PLAYER_O;
                    int score = minimax(board, 0, false);
                    board[i][j] = GameField.EMPTY;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[] { i, j };
                    }
                }
            }
        }

        if (bestMove == null) { // fallback
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == GameField.EMPTY)
                        return new int[] { i, j };
                }
            }
        }

        return bestMove;
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing) {
        Integer result = evaluateBoard(board);
        if (result != null)
            return result;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == GameField.EMPTY) {
                        board[i][j] = GameField.PLAYER_O;
                        bestScore = Math.max(bestScore, minimax(board, depth + 1, false));
                        board[i][j] = GameField.EMPTY;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == GameField.EMPTY) {
                        board[i][j] = GameField.PLAYER_X;
                        bestScore = Math.min(bestScore, minimax(board, depth + 1, true));
                        board[i][j] = GameField.EMPTY;
                    }
                }
            }
            return bestScore;
        }
    }

    private Integer evaluateBoard(int[][] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != GameField.EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                return board[i][0] == GameField.PLAYER_O ? 10 : -10;
            if (board[0][i] != GameField.EMPTY && board[0][i] == board[1][i] && board[1][i] == board[2][i])
                return board[0][i] == GameField.PLAYER_O ? 10 : -10;
        }
        if (board[0][0] != GameField.EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return board[0][0] == GameField.PLAYER_O ? 10 : -10;
        if (board[0][2] != GameField.EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            return board[0][2] == GameField.PLAYER_O ? 10 : -10;

        boolean hasEmpty = false;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == GameField.EMPTY)
                    hasEmpty = true;
            }
        }
        return hasEmpty ? null : 0;
    }

    @Override
    public boolean validateGameField(GameState oldState, GameState newState) {
        int[][] oldBoard = oldState.getGameField().getGameField();
        int[][] newBoard = newState.getGameField().getGameField();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (oldBoard[i][j] != GameField.EMPTY && oldBoard[i][j] != newBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isGameOver(GameState gameState) {
        return getWinnerMark(gameState) != null || isBoardFull(gameState);
    }

    @Override
    public Integer getWinnerMark(GameState gameState) {
        int[][] field = gameState.getGameField().getGameField();

        for (int i = 0; i < 3; i++) {
            if (field[i][0] != GameField.EMPTY && field[i][0] == field[i][1] && field[i][1] == field[i][2]) {
                return field[i][0];
            }
            if (field[0][i] != GameField.EMPTY && field[0][i] == field[1][i] && field[1][i] == field[2][i]) {
                return field[0][i];
            }
        }

        if (field[0][0] != GameField.EMPTY && field[0][0] == field[1][1] && field[1][1] == field[2][2]) {
            return field[0][0];
        }

        if (field[0][2] != GameField.EMPTY && field[0][2] == field[1][1] && field[1][1] == field[2][0]) {
            return field[0][2];
        }

        return null;
    }

    @Override
    public boolean isBoardFull(GameState gameState) {
        int[][] field = gameState.getGameField().getGameField();

        for (int[] row : field) {
            for (int cell : row) {
                if (cell == GameField.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

}
