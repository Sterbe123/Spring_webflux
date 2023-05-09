package cl.sterbe.app.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenErrorException extends Exception{
    public TokenErrorException(String message) {
        super(message);
    }
}
