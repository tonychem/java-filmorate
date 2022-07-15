package ru.yandex.praktikum.filmorate.exception;

public class NoSuchRatingException extends RuntimeException {
    public NoSuchRatingException(String message) {
        super(message);
    }
}
