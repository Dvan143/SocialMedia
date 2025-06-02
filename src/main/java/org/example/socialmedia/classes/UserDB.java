package org.example.socialmedia.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDB {
    NewsDB newsDB;
    @Autowired
    public UserDB(NewsDB newsDB) {
        this.newsDB = newsDB;
    }

    List<User> dbList = new ArrayList<>();
    int id = 0;
    // Users for testing
    {
        dbList.add(new User("admin@gmail.com","admin","admin",id++));
        dbList.add(new User("user@gmail.com","user","user",id++));
        dbList.add(new User("user2@yandex.ru","user2","user2",id++));
    }
    // Functions to user
    public void newUser(String mail, String name, String password) {
        dbList.add(new User(mail, name ,password, id++));
    }
    public boolean userExists(String name) {
        for (User user : dbList) {
            if (name.equals(user.getName())) {
                return true;
            }
        }
        return false;
    }
    public String getUserById(int id) {
        for (User user : dbList) {
            if (id == user.getId()) {
                return user.getName();
            }
        }
        return null;
    }
    // Functions to id
    public String getIdByName(String name) {
        for (User user : dbList) {
            if (name.equals(user.getName())) {
                return String.valueOf(user.getId());
            }
        }
        return null;
    }
    // Functions to email
    public String getEmailById(int id) {
        for (User user : dbList) {
            if (id == user.getId()) {
                return user.getEmail();
            }
        }
        return null;
    }
    public void resetPasswordByEmail(String mail) {
        for (User user : dbList) {
            if (mail.equals(user.getEmail())) {
                // do anything
            }
        }
    }
    public boolean emailExists(String email) {
        for (User user : dbList) {
            if (email.equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }
    // Functions to password
    public boolean passwordcheck(String name, String password) {
        for (User user : dbList) {
            if (name.equals(user.getName()) && password.equals(user.getPassword())) return true;
        }
        return false;
    }
    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }
    // Functions to friend
    public void addFriend(User user1, User user2) {
        user1.addFriend(user2);
    }
    public void removeFriend(User user1, User user2) {
        user1.removeFriend(user2);
    }
    // Functions to news feed
    public void addNews(String title, String content){
        //newsDB.addNews("");
    }
}
