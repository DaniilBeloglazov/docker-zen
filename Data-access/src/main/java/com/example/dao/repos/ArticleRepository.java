package com.example.dao.repos;

import com.example.dao.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
    boolean existsByTitle(String title);
    boolean existsByCreatedByAndTitle(String createdBy, String title);
    void deleteByTitle(String titleOfArticle);
    Optional<Article> findByTitle(String titleOfArticle);
    List<Article> findByCreatedBy(String currentUser);
}
