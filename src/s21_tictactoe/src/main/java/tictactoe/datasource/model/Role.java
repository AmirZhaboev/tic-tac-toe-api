package tictactoe.datasource.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER {
        public String getAuthority() {
            return "USER";
        }
    };

}
