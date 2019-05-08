package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import se.elfu.sportprojectbackend.exception.ErrorDetails;
import se.elfu.sportprojectbackend.exception.customException.BadRequestException;
import se.elfu.sportprojectbackend.exception.customException.InvalidTokenException;
import se.elfu.sportprojectbackend.exception.customException.ListEmptyException;
import se.elfu.sportprojectbackend.exception.customException.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleNotFoundException(NotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "NOT FOUND",
                Arrays.asList(ex.getMessage()));
        log.info(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ListEmptyException.class)
    public final ResponseEntity<ErrorDetails> handleListEmptyException(ListEmptyException ex) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "NOT FOUND",
                Arrays.asList(ex.getMessage()));
        log.info(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException ex) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "BAD REQUEST",
                Arrays.asList(ex.getMessage()));
        log.info(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleException(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "INTERNAL SERVER ERROR",
                Arrays.asList(ex.getMessage()));
        log.error("{}", ex.getLocalizedMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public final ResponseEntity<ErrorDetails> handleInvalidTokenException(InvalidTokenException ex) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "INVALID TOKEN",
                Arrays.asList(ex.getMessage()));
        log.error("{}", ex.getLocalizedMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorDetails error = new ErrorDetails(LocalDateTime.now() ,"BAD REQUEST", details);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

}
