package tictactoe.web.mapper;

import org.springframework.stereotype.Component;

import tictactoe.datasource.model.UserEntity;
import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameField;
import tictactoe.domain.model.GameState;
import tictactoe.web.model.GameFieldDto;
import tictactoe.web.model.GameResponse;
import tictactoe.web.model.GameStateDto;
import tictactoe.web.model.UserResponse;

@Component
public class GameWebMapper {

    public GameResponse toResponse(Game game) {
        GameResponse response = new GameResponse();

        response.setGameId(game.getGameId());
        response.setVsComputer(game.getGameState().isVsComputer());
        response.setPlayerXId(game.getGameState().getPlayerXId());
        response.setPlayerOId(game.getGameState().getPlayerOId());
        response.setStatus(game.getGameState().getStatus());
        response.setCurrentPlayerId(game.getGameState().getCurrentPlayerId());
        response.setWinnerId(game.getGameState().getWinnerId());
        response.setBoard(game.getGameState().getGameField().getGameField());
        response.setLastTurnX(game.getGameState().getLastTurnX());
        response.setLastTurnY(game.getGameState().getLastTurnY());
        response.setCreatedAt(game.getGameState().getCreatedAt());

        return response;
    }

    public UserResponse toUserResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(userEntity.getId());
        userResponse.setLogin(userEntity.getLogin());

        return userResponse;
    }
}
