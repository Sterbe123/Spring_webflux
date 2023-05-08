package cl.sterbe.app.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenError extends Exception{

    private String mensaje;

    public TokenError(String mensaje){
        this.mensaje = mensaje;
    }
}
