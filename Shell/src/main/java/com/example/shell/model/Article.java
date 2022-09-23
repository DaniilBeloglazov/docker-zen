package com.example.shell.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    private String id;

    private String title;

    private String description;

    public String createdBy;

    public Date createdDate;
}
