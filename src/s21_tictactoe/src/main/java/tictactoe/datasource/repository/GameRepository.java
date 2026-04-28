package tictactoe.datasource.repository;

import tictactoe.datasource.model.GameEntity;
import tictactoe.datasource.projection.WinRatioProjection;
import tictactoe.domain.model.GameStatus;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends CrudRepository<GameEntity, UUID> {
    List<GameEntity> findByGameStateEntity_VsComputerFalseAndGameStateEntity_Status(GameStatus status);

    @Query("select gameEntity from GameEntity gameEntity where (gameEntity.gameStateEntity.playerXId = :id or gameEntity.gameStateEntity.playerOId = :id) and (gameEntity.gameStateEntity.winnerId = :id or gameEntity.gameStateEntity.status = :status)")
    List<GameEntity> findEndedGames(@Param("id") UUID id, @Param("status") GameStatus status);

    @Query(value = """
            WITH player_stats AS (
                SELECT
                    gs.playerxid AS user_id,
                    CASE WHEN gs.winner_id = gs.playerxid THEN 1 ELSE 0 END AS wins,
                    CASE WHEN gs.status = 'DRAW' THEN 1 ELSE 0 END AS draws,
                    CASE
                        WHEN gs.winner_id IS NOT NULL
                             AND gs.winner_id <> gs.playerxid
                             AND gs.status <> 'DRAW'
                        THEN 1 ELSE 0
                    END AS losses
                FROM games_states gs
                WHERE gs.winner_id IS NOT NULL OR gs.status = 'DRAW'

                UNION ALL

                SELECT
                    gs.playeroid AS user_id,
                    CASE WHEN gs.winner_id = gs.playeroid THEN 1 ELSE 0 END AS wins,
                    CASE WHEN gs.status = 'DRAW' THEN 1 ELSE 0 END AS draws,
                    CASE
                        WHEN gs.winner_id IS NOT NULL
                             AND gs.winner_id <> gs.playeroid
                             AND gs.status <> 'DRAW'
                        THEN 1 ELSE 0
                    END AS losses
                FROM games_states gs
                WHERE gs.playeroid IS NOT NULL
                  AND (gs.winner_id IS NOT NULL OR gs.status = 'DRAW')
            ),
            aggregated AS (
                SELECT
                    user_id,
                    SUM(wins) AS wins,
                    SUM(draws) AS draws,
                    SUM(losses) AS losses
                FROM player_stats
                GROUP BY user_id
            )
            SELECT
                a.user_id AS userId,
                u.login AS login,
                CASE
                    WHEN (a.losses + a.draws) = 0 THEN a.wins::numeric
                    ELSE ROUND(a.wins::numeric / (a.losses + a.draws), 4)
                END AS winRatio
            FROM aggregated a
            JOIN users u ON u.id = a.user_id
            ORDER BY winRatio DESC, u.login
            LIMIT :n
            """, nativeQuery = true)
    List<WinRatioProjection> findTopPlayers(@Param("n") int n);
}
