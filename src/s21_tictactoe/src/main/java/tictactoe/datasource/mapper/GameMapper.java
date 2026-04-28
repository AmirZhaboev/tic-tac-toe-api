package tictactoe.datasource.mapper;

import org.springframework.stereotype.Component;

import tictactoe.datasource.model.GameEntity;
import tictactoe.datasource.model.GameFieldEntity;
import tictactoe.datasource.model.GameStateEntity;
import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameField;
import tictactoe.domain.model.GameState;

@Component
public class GameMapper {

    public GameEntity toEntity(Game game) {
        GameFieldEntity fieldEntity = new GameFieldEntity();
        int[][] board = game.getGameState().getGameField().getGameField();

        fieldEntity.setCell00(board[0][0]);
        fieldEntity.setCell01(board[0][1]);
        fieldEntity.setCell02(board[0][2]);
        fieldEntity.setCell10(board[1][0]);
        fieldEntity.setCell11(board[1][1]);
        fieldEntity.setCell12(board[1][2]);
        fieldEntity.setCell20(board[2][0]);
        fieldEntity.setCell21(board[2][1]);
        fieldEntity.setCell22(board[2][2]);

        GameStateEntity stateEntity = new GameStateEntity();
        stateEntity.setId(game.getGameId());
        stateEntity.setGameFieldEntity(fieldEntity);
        stateEntity.setVsComputer(game.getGameState().isVsComputer());
        stateEntity.setPlayerXId(game.getGameState().getPlayerXId());
        stateEntity.setPlayerOId(game.getGameState().getPlayerOId());
        stateEntity.setStatus(game.getGameState().getStatus());
        stateEntity.setCurrentPlayerId(game.getGameState().getCurrentPlayerId());
        stateEntity.setWinnerId(game.getGameState().getWinnerId());
        stateEntity.setLastTurnX(game.getGameState().getLastTurnX());
        stateEntity.setLastTurnY(game.getGameState().getLastTurnY());
        stateEntity.setCreatedAt(game.getGameState().getCreatedAt());

        GameEntity entity = new GameEntity();
        entity.setGameId(game.getGameId());
        entity.setGameStateEntity(stateEntity);

        return entity;
    }

    public Game toGame(GameEntity gameEntity) {
        GameFieldEntity fieldEntity = gameEntity.getGameStateEntity().getGameFieldEntity();
        int[][] board = new int[3][3];

        board[0][0] = fieldEntity.getCell00();
        board[0][1] = fieldEntity.getCell01();
        board[0][2] = fieldEntity.getCell02();
        board[1][0] = fieldEntity.getCell10();
        board[1][1] = fieldEntity.getCell11();
        board[1][2] = fieldEntity.getCell12();
        board[2][0] = fieldEntity.getCell20();
        board[2][1] = fieldEntity.getCell21();
        board[2][2] = fieldEntity.getCell22();

        GameField field = new GameField();
        field.setGameField(board);

        GameState state = new GameState();
        state.setGameField(field);
        state.setVsComputer(gameEntity.getGameStateEntity().isVsComputer());
        state.setPlayerXId(gameEntity.getGameStateEntity().getPlayerXId());
        state.setPlayerOId(gameEntity.getGameStateEntity().getPlayerOId());
        state.setStatus(gameEntity.getGameStateEntity().getStatus());
        state.setCurrentPlayerId(gameEntity.getGameStateEntity().getCurrentPlayerId());
        state.setWinnerId(gameEntity.getGameStateEntity().getWinnerId());
        state.setLastTurnX(gameEntity.getGameStateEntity().getLastTurnX());
        state.setLastTurnY(gameEntity.getGameStateEntity().getLastTurnY());
        state.setCreatedAt(gameEntity.getGameStateEntity().getCreatedAt());

        return new Game(gameEntity.getGameId(), state);
    }
}
