package cl.sterbe.app.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SamePasswordsException extends Exception{

    public SamePasswordsException(){}
}
