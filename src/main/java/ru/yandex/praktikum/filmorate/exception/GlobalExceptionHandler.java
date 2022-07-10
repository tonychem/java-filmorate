package ru.yandex.praktikum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice(basePackages = "ru.yandex.praktikum.filmorate")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationExceptions(ValidationException exc) {
        log.warn(exc.getMessage());
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exc) {
        log.warn(exc.getMessage());
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NoSuchFilmException.class, NoSuchUserException.class,
                                NoSuchRatingException.class, NoSuchGenreException.class})
    public ResponseEntity<String> handleObjectMissingExceptions(RuntimeException exc) {
        log.warn(exc.getMessage());
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {FilmAlreadyExistsException.class, UserAlreadyExistsException.class})
    public ResponseEntity<String> handleEntityAlreadyExistsExceptions(RuntimeException exc) {
        log.warn(exc.getMessage());
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException exc) {
        log.warn(exc.getMessage());
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> handleNullPointerExceptions(RuntimeException exc) {
        log.warn(exc.getMessage());
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
