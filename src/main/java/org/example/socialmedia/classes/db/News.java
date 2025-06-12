package org.example.socialmedia.classes.db;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String date;
    @Column
    private String title;
    @Column
    private String content;

    @ManyToOne()
    @JoinColumn(name = "author_id")
    private UserClass author;

    public News(String date, String title, String content, UserClass author) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorName() {
        return author.getUsername();
    }

    public UserClass getAuthor() {
        return author;
    }

    public News() {

    }

    @Override
    public String toString() {
        return date + "\n" + title + "\n" + content + "\n" + author + "\n";
    }

}
