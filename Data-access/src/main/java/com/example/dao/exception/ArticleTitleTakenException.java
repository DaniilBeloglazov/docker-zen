package com.example.dao.exception;

public class ArticleTitleTakenException extends RuntimeException{
    public ArticleTitleTakenException(String message) {
        super(message);
    }
}
