package se.elfu.sportprojectbackend.exception.customException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ListEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ListEmptyException(){
        super("Kunde inte hitta några poster" );
    }
}
