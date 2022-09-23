package com.example.dao.model;

import com.example.dao.audit.AuditMetadata;
import com.example.dao.dto.CreateArticleRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Article extends AuditMetadata {

    @Id
    private String id;

    private String title;

    private String description;

    public Article(CreateArticleRequest request) {
        title = request.getTitle();
        description = request.getDescription();
    }
}
