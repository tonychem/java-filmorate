package ru.yandex.praktikum.filmorate.exception;

public class FilmAlreadyExistsException extends RuntimeException {
    public FilmAlreadyExistsException(String message) {
        super(message);
    }
}
