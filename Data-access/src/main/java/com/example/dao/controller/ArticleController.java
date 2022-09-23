package com.example.dao.controller;

import com.example.dao.dto.CreateArticleRequest;
import com.example.dao.dto.UpdateArticleRequest;
import com.example.dao.model.Article;
import com.example.dao.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{title}")
    public ResponseEntity<Article> showArticle(@PathVariable("title") String title) {
        val article = articleService.findArticle(title);
        return ResponseEntity.ok(article);
    }

    @GetMapping("/own")
    public ResponseEntity<List<String>> showArticleNames(){
        val names = articleService.showOwnArticleNames();
        return ResponseEntity.ok(names);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createArticle(@RequestBody CreateArticleRequest request){
        log.info("handle attempt to create Article");
        val msg = articleService.save(new Article(request));
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<String> deleteArticle(@PathVariable("title") String titleOfArticle){
        val msg = articleService.deleteOwnArticle(titleOfArticle);
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/{title}")
    public ResponseEntity<String> updateArticle(@PathVariable("title") String titleOfArticle, @RequestBody UpdateArticleRequest request){
        val msg = articleService.updateOwnArticle(titleOfArticle, request.getNewDescription());
        return ResponseEntity.ok(msg);
    }
}
