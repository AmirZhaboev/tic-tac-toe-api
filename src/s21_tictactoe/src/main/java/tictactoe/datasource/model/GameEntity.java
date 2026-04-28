package tictactoe.datasource.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "games")
public class GameEntity {
    @Id
    private UUID gameId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_state_id")
    private GameStateEntity gameStateEntity;

    public GameEntity(GameStateEntity gameStateEntity) {
        this.gameStateEntity = gameStateEntity;
    }

    public GameEntity(UUID gameId, GameStateEntity gameStateEntity) {
        this.gameId = gameId;
        this.gameStateEntity = gameStateEntity;
    }
}
