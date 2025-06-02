package org.example.socialmedia.classes;

import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Service
public class NewsDB {
    List<News> newsList = new LinkedList<>();

    // Adding News with explicit indication of this date
    public void addNews(String title, String content, String author) {
        // Getting actual time and date
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("GMT+3"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formattedDate = zonedDateTime.format(formatter);

        newsList.add(new News(formattedDate, title, content, author));
    }
    // Adding News without explicit indication of this date
    public void addNews(String date, String title, String content, String author) {
        newsList.add(new News(date, title, content, author));
    }

    public List[] getNewsList(){
        LinkedList<String> allNews = new LinkedList<>();
        LinkedList<String> lastNews = new LinkedList<>();

        for(int i = 0; i < newsList.size(); i++){
            allNews.addFirst(newsList.get(i).toString());
            // Getting 3 least news from newsList
            if(i > newsList.size()-4) lastNews.addFirst(newsList.get(i).toString());
        }
        return new List[]{allNews, lastNews};
    }
    // init data
    {
        // with date
        addNews("12.02.2023 07:13:45","Hello world", "World has been hellowed", "Jo biden))");
        addNews("17.12.2023 09:17:22","Hello world", "World has been hellowed again", "Jo biden))");
        addNews("28.12.2024 20:42:29","Hello world", "World has been hellowed again too", "Jo biden))");
        // without date
        addNews("Hello world", "World has been hellowed again too", "Jo biden))");
    }
}