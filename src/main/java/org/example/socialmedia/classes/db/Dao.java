package org.example.socialmedia.classes.db;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Dao {
    @Autowired
    EntityManager entityManager;


    // User
    @Transactional
    public void saveUser(UserClass userClass){
        entityManager.persist(userClass);
    }

    @Transactional(readOnly = true)
    public UserClass getUserByUsername(String username) {
        List<UserClass> result = entityManager.createQuery("SELECT u FROM UserClass u WHERE u.username = :username", UserClass.class).setParameter("username",username).getResultList();
        if(result.isEmpty()) throw new UsernameNotFoundException("User not found");
        return result.get(0);
    }

    @Transactional(readOnly = true)
    public String isUsernameOrEmailExist(String username, String email) {
        List<Object[]> result = entityManager.createQuery("SELECT u.username, u.email FROM UserClass u WHERE u.username = :username OR u.email = :email", Object[].class).setParameter("username", username).setParameter("email", email).getResultList();

        boolean usernameExists = false;
        boolean emailExists = false;

        for (Object[] row : result) {
            String dbUsername = (String) row[0];
            String dbEmail = (String) row[1];

            if (username.equals(dbUsername)) {
                usernameExists = true;
            }
            if (email.equals(dbEmail)) {
                emailExists = true;
            }
        }

        if (usernameExists && emailExists) return "Username&Email";
        if (usernameExists) return "Username";
        if (emailExists) return "Email";
        return "None";
    }

    @Transactional(readOnly = true)
    public List<UserClassDto> getUsersByPage(int page){
        return entityManager.createQuery("SELECT u.username, u.email, n.title, u.role FROM UserClass u JOIN u.news n", UserClassDto.class).setFirstResult((page-1) * 10).setMaxResults(10).getResultList();
    }


    // News
    @Transactional
    public void saveNews(News news){
        entityManager.persist(news);
    }
    @Transactional(readOnly = true)
    public List<News> getNewsByUsername(String username){
        return entityManager.createQuery("SELECT n FROM News n JOIN n.author a WHERE a.username = :username", News.class).setParameter("username",username).getResultList();
    }
    @Transactional
    public String getAuthorUsernameOfNews(News news){
        return entityManager.createQuery("SELECT a.username FROM News n JOIN n.author a WHERE n= :news", String.class).setParameter("news",news).getSingleResult();
    }
    @Transactional(readOnly = true)
    public List<NewsDto> getAllNews(){
        return entityManager.createQuery("SELECT n.date,n.title,n.content, n.author.username FROM News n", NewsDto.class).getResultList();
    }
}