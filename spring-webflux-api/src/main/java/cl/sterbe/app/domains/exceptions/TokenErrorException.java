package cl.sterbe.app.domains.exceptions;

import io.jsonwebtoken.JwtException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenErrorException extends JwtException {
    public TokenErrorException(String message) {
        super(message);
    }
}
