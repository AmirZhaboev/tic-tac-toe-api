package tictactoe.web.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeaderBoardResponse {
    private UUID userId;
    private String login;
    private BigDecimal winRatio;
}
