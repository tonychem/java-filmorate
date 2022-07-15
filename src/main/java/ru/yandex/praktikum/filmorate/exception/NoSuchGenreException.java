package ru.yandex.praktikum.filmorate.exception;

public class NoSuchGenreException extends RuntimeException {
    public NoSuchGenreException(String message) {
        super(message);
    }
}
