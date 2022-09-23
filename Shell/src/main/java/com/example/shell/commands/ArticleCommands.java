package com.example.shell.commands;

import com.example.shell.config.Authentication;
import com.example.shell.dto.CreateArticleRequest;
import com.example.shell.dto.UpdateArticleRequest;
import com.example.shell.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Scanner;

@ShellComponent
@ShellCommandGroup("Article")
@RequiredArgsConstructor
public class ArticleCommands {

    private final Authentication authentication;

    private final RestTemplate restTemplate;

    @ShellMethod("Shows full info about specific article")
    public void viewArticle(String title){
        val request = new HttpEntity<>(getHeadersWithTokens());
        val response = restTemplate.exchange("http://localhost:8082/article/" + title, HttpMethod.GET, request, Article.class);
        val article = response.getBody();
        System.out.println("Title: " + article.getTitle());
        System.out.println("Description: " + article.getDescription());
        System.out.println("Created by: " + article.getCreatedBy());
        System.out.println("Created date: " + article.getCreatedDate().toString());
    }

    @ShellMethod("Show the titles of your articles")
    public void showOwnArticles() {
        val request = new HttpEntity<>(getHeadersWithTokens());
        val response = restTemplate.exchange("http://localhost:8082/article/own", HttpMethod.GET, request, List.class);
        if (response.getBody().isEmpty()) {
            System.out.println("You dont have articles");
            return;
        }
        response.getBody()
                .forEach((name) -> {
                    System.out.println("------ " + name + " ------");
                });
    }

    @ShellMethod("Create article. Takes no args")
    public void createArticle() {
        val console = new Scanner(System.in);
        System.out.print("Type title: ");
        val title = console.nextLine();
        System.out.print("Type new description: ");
        val newDescription = console.nextLine();
        val headers = getHeadersWithTokens();
        val request = new HttpEntity<>(new CreateArticleRequest(title, newDescription), headers);
        val responce = restTemplate.postForEntity("http://localhost:8082/article/new", request, String.class);
        System.out.println(responce.getBody());
    }

    @ShellMethod("Update article")
    public String updateArticle() {
        val console = new Scanner(System.in);
        System.out.println("Type title of article to update or press enter to abort command: ");
        val title = console.nextLine();
        if (title.isEmpty())
            return "Command aborted";
        System.out.println("Type new description of press enter to abort command: ");
        val newDescription = console.nextLine();
        if (newDescription.isEmpty())
            return "Command aborted";
        //
        val headers = getHeadersWithTokens();
        val request = new HttpEntity<>(new UpdateArticleRequest(newDescription), headers);
        val response = restTemplate.exchange("http://localhost:8082/article/" + title, HttpMethod.PATCH, request, String.class);
        return response.getBody();
    }

    @ShellMethod("Deletes article of currently authenticated user. Takes one arg: title to delete")
    public void deleteArticle(String title) {
        val headers = getHeadersWithTokens();
        val request = new HttpEntity<>(headers);
        val response = restTemplate.exchange("http://localhost:8082/article/" + title, HttpMethod.DELETE, request, String.class);
        System.out.println(response.getBody());
    }

    public HttpHeaders getHeadersWithTokens() {
        val headers = new HttpHeaders();
        headers.add("accessToken", authentication.getAccessToken());
        headers.add("refreshToken", authentication.getRefreshToken());
        return headers;
    }
}
