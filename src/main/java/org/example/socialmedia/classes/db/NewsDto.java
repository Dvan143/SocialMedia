package org.example.socialmedia.classes.db;

public class NewsDto {
    private String date;
    private String title;
    private String content;
    private String author;

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public NewsDto(String date, String title, String content, String author) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
