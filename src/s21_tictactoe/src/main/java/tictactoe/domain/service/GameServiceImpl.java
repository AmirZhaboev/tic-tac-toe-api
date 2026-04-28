package tictactoe.domain.service;

import tictactoe.datasource.mapper.GameMapper;
import tictactoe.datasource.model.GameEntity;
import tictactoe.datasource.projection.WinRatioProjection;
import tictactoe.datasource.repository.GameRepository;
import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameField;
import tictactoe.domain.model.GameState;
import tictactoe.domain.model.GameStatus;
import tictactoe.web.model.LeaderBoardResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final GameLogicService gameLogicService;

    public GameServiceImpl(GameRepository gameRepository, GameMapper gameMapper, GameLogicService gameLogicService) {
        this.gameRepository = gameRepository;
        this.gameLogicService = gameLogicService;
        this.gameMapper = gameMapper;
    }

    @Override
    public UUID createNewGame(UUID creatorId, boolean vsComputer) {
        GameField field = new GameField();
        GameState state = new GameState(field);

        state.setVsComputer(vsComputer);
        state.setPlayerXId(creatorId);
        state.setPlayerOId(null);
        state.setWinnerId(null);
        state.setLastTurnX(-1);
        state.setLastTurnY(-1);

        if (vsComputer) {
            state.setStatus(GameStatus.PLAYER_TURN);
            state.setCurrentPlayerId(creatorId);
        } else {
            state.setStatus(GameStatus.WAITING_FOR_PLAYERS);
            state.setCurrentPlayerId(null);
        }

        Game game = new Game(state);

        gameRepository.save(gameMapper.toEntity(game));

        return game.getGameId();
    }

    @Override
    public List<Game> getAvailableGames() {
        List<GameEntity> entities = gameRepository
                .findByGameStateEntity_VsComputerFalseAndGameStateEntity_Status(GameStatus.WAITING_FOR_PLAYERS);
        List<Game> result = new ArrayList<>();
        for (GameEntity entity : entities) {
            result.add(gameMapper.toGame(entity));
        }

        return result;
    }

    @Override
    public Game joinGame(UUID gameId, UUID userId) {
        GameEntity gameEntity = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Игра не найдена"));
        Game game = gameMapper.toGame(gameEntity);
        GameState gameState = game.getGameState();

        if (gameState.isVsComputer()) {
            throw new IllegalArgumentException("Нельзя присоединиться к игре с компьютером");
        }

        if (gameState.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            throw new IllegalArgumentException("Игра недоступна для присоединения");
        }

        if (gameState.getPlayerXId() != null && gameState.getPlayerXId().equals(userId)) {
            throw new IllegalArgumentException("Нельзя присоединиться к собственной игре");
        }

        gameState.setPlayerOId(userId);
        gameState.setStatus(GameStatus.PLAYER_TURN);
        gameState.setCurrentPlayerId(gameState.getPlayerXId());

        gameRepository.save(gameMapper.toEntity(game));

        return game;
    }

    @Override
    public Game makeMove(UUID gameId, UUID userId, int x, int y) {
        GameEntity gameEntity = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Игра не найдена"));
        Game game = gameMapper.toGame(gameEntity);
        GameState gameState = game.getGameState();

        int[][] board = gameState.getGameField().getGameField();
        validateMove(gameState, userId, x, y);

        int playerMark = resolvePlayerMark(gameState, userId);

        board[x][y] = playerMark;
        gameState.setLastTurnX(x);
        gameState.setLastTurnY(y);

        updateGameStatusAfterMove(gameState);

        if (gameState.getStatus() == GameStatus.WIN || gameState.getStatus() == GameStatus.DRAW) {
            gameRepository.save(gameMapper.toEntity(game));
            return game;
        }

        if (gameState.isVsComputer()) {
            int[] computerMove = gameLogicService.getNextMove(gameState);
            if (computerMove != null) {
                board[computerMove[0]][computerMove[1]] = GameField.PLAYER_O;
                gameState.setLastTurnX(computerMove[0]);
                gameState.setLastTurnY(computerMove[1]);

                updateGameStatusAfterComputerMove(gameState);

                if (gameState.getStatus() != GameStatus.WIN && gameState.getStatus() != GameStatus.DRAW) {
                    gameState.setStatus(GameStatus.PLAYER_TURN);
                    gameState.setCurrentPlayerId(gameState.getPlayerXId());
                }
            }
        } else {
            UUID nextPlayer = userId.equals(gameState.getPlayerXId()) ? gameState.getPlayerOId()
                    : gameState.getPlayerXId();

            gameState.setStatus(GameStatus.PLAYER_TURN);
            gameState.setCurrentPlayerId(nextPlayer);
        }

        gameRepository.save(gameMapper.toEntity(game));

        return game;

    }

    private void validateMove(GameState state, UUID userId, int x, int y) {
        if (state.getStatus() == GameStatus.WAITING_FOR_PLAYERS) {
            throw new IllegalArgumentException("Игра ожидает второго игрока");
        }

        if (state.getStatus() == GameStatus.WIN || state.getStatus() == GameStatus.DRAW) {
            throw new IllegalArgumentException("Игра уже завершена");
        }

        if (state.getCurrentPlayerId() == null || !state.getCurrentPlayerId().equals(userId)) {
            throw new IllegalArgumentException("Сейчас не ход этого игрока");
        }

        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw new IllegalArgumentException("Некорректные координаты");
        }

        int[][] board = state.getGameField().getGameField();
        if (board[x][y] != GameField.EMPTY) {
            throw new IllegalArgumentException("Клетка уже занята");
        }
    }

    private int resolvePlayerMark(GameState state, UUID userId) {
        if (userId.equals(state.getPlayerXId())) {
            return GameField.PLAYER_X;
        }

        if (userId.equals(state.getPlayerOId())) {
            return GameField.PLAYER_O;
        }

        throw new IllegalArgumentException("Пользователь не участвует в игре");
    }

    private void updateGameStatusAfterMove(GameState state) {
        Integer winnerMark = gameLogicService.getWinnerMark(state);

        if (winnerMark != null) {
            state.setStatus(GameStatus.WIN);

            if (winnerMark == GameField.PLAYER_X) {
                state.setWinnerId(state.getPlayerXId());
            } else if (winnerMark == GameField.PLAYER_O) {
                state.setWinnerId(state.getPlayerOId());
            }

            state.setCurrentPlayerId(null);
            return;
        }

        if (gameLogicService.isBoardFull(state)) {
            state.setStatus(GameStatus.DRAW);
            state.setWinnerId(null);
            state.setCurrentPlayerId(null);
        }
    }

    private void updateGameStatusAfterComputerMove(GameState state) {
        Integer winnerMark = gameLogicService.getWinnerMark(state);

        if (winnerMark != null) {
            state.setStatus(GameStatus.WIN);

            if (winnerMark == GameField.PLAYER_X) {
                state.setWinnerId(state.getPlayerXId());
            } else {
                state.setWinnerId(null);
            }

            state.setCurrentPlayerId(null);
            return;
        }

        if (gameLogicService.isBoardFull(state)) {
            state.setStatus(GameStatus.DRAW);
            state.setWinnerId(null);
            state.setCurrentPlayerId(null);
        }
    }

    @Override
    public Game getGame(UUID gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Игра не найдена"));
        return gameMapper.toGame(gameEntity);
    }

    @Override
    public List<Game> getEndedGamesById(UUID userId) {
        List<GameEntity> gameEntities = gameRepository.findEndedGames(userId, GameStatus.DRAW);
        List<Game> games = new ArrayList<>();
        for (GameEntity g : gameEntities) {
            games.add(gameMapper.toGame(g));
        }
        return games;
    }

    @Override
    public List<LeaderBoardResponse> getTopPlayers(int n) {
        List<WinRatioProjection> topPlayers = gameRepository.findTopPlayers(n);
        List<LeaderBoardResponse> responses = new ArrayList<>();

        for (WinRatioProjection player : topPlayers) {
            LeaderBoardResponse response = new LeaderBoardResponse();
            response.setUserId(player.getUserId());
            response.setLogin(player.getLogin());
            response.setWinRatio(player.getWinRatio());
            responses.add(response);
        }

        return responses;
    }

}
