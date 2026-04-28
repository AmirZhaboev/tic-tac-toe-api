package tictactoe.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshJwtRequest {
    private String refreshToken;
}
