package tictactoe.domain.service;

import org.springframework.stereotype.Service;

import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameState;
import tictactoe.web.model.LeaderBoardResponse;

import java.util.List;
import java.util.UUID;

public interface GameService {
    UUID createNewGame(UUID creatorId, boolean vsComputer);

    List<Game> getAvailableGames();

    Game joinGame(UUID gameId, UUID userId);

    Game makeMove(UUID gameId, UUID userId, int x, int y);

    Game getGame(UUID gameId);

    List<Game> getEndedGamesById(UUID userId);

    List<LeaderBoardResponse> getTopPlayers(int n);
}
