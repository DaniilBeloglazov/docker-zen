package com.example.dao.service;

import com.example.dao.exception.ArticleNotFoundException;
import com.example.dao.exception.ArticleTitleTakenException;
import com.example.dao.model.Article;
import com.example.dao.repos.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<String> showOwnArticleNames() {
        val currentUser = getCurrentUsername();
        List<Article> articles = articleRepository.findByCreatedBy(currentUser);
        List<String> names = new ArrayList<>();
        articles.forEach(article -> {
            names.add(article.getTitle());
        });
        return names;
    }

    public String save(Article articleToSave) {
        if (articleRepository.existsByTitle(articleToSave.getTitle()))
            throw new ArticleTitleTakenException("Title " + articleToSave.getTitle() + " taken");
        articleRepository.save(articleToSave);
        return "Article " + articleToSave.getTitle() + " was saved";
    }

    public String deleteOwnArticle(String titleOfArticle) {
        val currentUser = getCurrentUsername();
        if (!articleRepository.existsByCreatedByAndTitle(currentUser, titleOfArticle)) {
            throw new ArticleNotFoundException("You dont have such article");
        }
        articleRepository.deleteByTitle(titleOfArticle);
        return "Your article " + titleOfArticle + " was deleted";
    }

    public String updateOwnArticle(String titleOfArticle, String newDescription) {
        val currentUser = getCurrentUsername();
        val articleToUpdate = articleRepository.findByTitle(titleOfArticle)
                .orElseThrow(() -> new ArticleNotFoundException("Article " + titleOfArticle + " not found"));
        if (!articleToUpdate.getCreatedBy().equals(currentUser))
            throw new ArticleNotFoundException("You dont have such article");
        articleToUpdate.setDescription(newDescription);
        articleRepository.save(articleToUpdate);
        return "Article " + titleOfArticle + " was updated";
    }

    public String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();
    }

    public Article findArticle(String title) {
        return articleRepository.findByTitle(title)
                .orElseThrow(() -> new ArticleNotFoundException("Article " + title + " not found"));
    }
}
