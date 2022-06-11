package ru.yandex.praktikum.filmorate.service;

import lombok.NonNull;

public class FriendListEmptyException extends RuntimeException {
    public FriendListEmptyException(String message) {
        super(message);
    }
}
