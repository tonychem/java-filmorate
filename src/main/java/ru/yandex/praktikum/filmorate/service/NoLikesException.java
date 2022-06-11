package ru.yandex.praktikum.filmorate.service;

public class NoLikesException extends RuntimeException {
    public NoLikesException(String message) {
        super(message);
    }
}
