package tictactoe.datasource.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface WinRatioProjection {
    UUID getUserId();

    String getLogin();

    BigDecimal getWinRatio();
}
