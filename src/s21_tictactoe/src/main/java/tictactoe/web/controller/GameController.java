package tictactoe.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tictactoe.domain.model.GameState;
import tictactoe.domain.model.GameStatus;
import tictactoe.domain.service.GameLogicService;
import tictactoe.domain.service.GameService;
import tictactoe.domain.service.UserService;
import tictactoe.web.mapper.GameWebMapper;
import tictactoe.web.model.CreateGameRequest;
import tictactoe.web.model.GameResponse;
import tictactoe.web.model.GameStateDto;
import tictactoe.web.model.MoveRequest;
import tictactoe.datasource.model.UserEntity;
import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameField;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final GameWebMapper gameWebMapper;

    public GameController(GameService gameService, GameLogicService gameLogicService, GameWebMapper gameWebMapper) {
        this.gameService = gameService;
        this.gameWebMapper = gameWebMapper;
    }

    @PostMapping
    public ResponseEntity<String> createGame(@RequestBody CreateGameRequest request) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID id = gameService.createNewGame(currentUserId, request.isVsComputer());

        return ResponseEntity.status(HttpStatus.CREATED).body(id.toString());
    }

    @GetMapping("/available")
    public ResponseEntity<List<GameResponse>> getAvailableGames() {
        List<Game> games = gameService.getAvailableGames();
        List<GameResponse> gameResponses = new ArrayList<>();

        for (Game game : games) {
            gameResponses.add(gameWebMapper.toResponse(game));
        }
        return ResponseEntity.ok(gameResponses);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGame(@PathVariable UUID gameId) {
        Game game = gameService.getGame(gameId);
        return ResponseEntity.ok(gameWebMapper.toResponse(game));
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<?> joinGame(@PathVariable UUID gameId) {
        try {
            UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Game game = gameService.joinGame(gameId, currentUserId);
            return ResponseEntity.ok(gameWebMapper.toResponse(game));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<?> makeMove(@PathVariable UUID gameId, @RequestBody MoveRequest request) {
        try {
            UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Game game = gameService.makeMove(gameId, currentUserId, request.getX(), request.getY());
            return ResponseEntity.ok(gameWebMapper.toResponse(game));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/ended")
    public ResponseEntity<?> getEndedGames() {
        try {
            UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Game> games = gameService.getEndedGamesById(currentUserId);
            List<GameResponse> gameResponses = new ArrayList<>();
            for (Game g : games) {
                gameResponses.add(gameWebMapper.toResponse(g));
            }
            return ResponseEntity.ok(gameResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getTopPlayers(@RequestParam int n) {
        try {
            return ResponseEntity.ok(gameService.getTopPlayers(n));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
