package com.example.dao.controller;

import com.example.dao.exception.ArticleNotFoundException;
import com.example.dao.exception.ArticleTitleTakenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(ArticleTitleTakenException.class)
    public ResponseEntity<String> handleInvalidCreationArticle(ArticleTitleTakenException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<String> handleArticleNotFound(ArticleNotFoundException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
