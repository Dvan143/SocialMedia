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
        // Check is user exist
        if(entityManager.createQuery("SELECT u FROM UserClass u WHERE u.username= :username OR u.email= :email").setParameter("username",userClass.getUsername()).setParameter("email",userClass.getEmail()).getResultList().isEmpty()) {
            entityManager.persist(userClass);
            entityManager.flush();
        }
    }

    @Transactional(readOnly = true)
    public UserClass getUserByUsername(String username) {
        List<UserClass> result = entityManager.createQuery("SELECT u FROM UserClass u WHERE u.username = :username", UserClass.class).setParameter("username",username).getResultList();
        if(result.isEmpty()) throw new UsernameNotFoundException("User not found");
        return result.get(0);
    }

    @Transactional(readOnly = true)
    public List<UserClassDto> getUsersByPage(int page){
        if (page < 1) page = 1;
        int offset = (page - 1) * 10;
        return entityManager.createQuery("SELECT u.username, u.email, n.title, u.role FROM UserClass u LEFT JOIN u.news n", UserClassDto.class).setFirstResult(offset).setMaxResults(10).getResultList();
    }

    @Transactional
    public boolean isUserExist(String username){
        return !(entityManager.createQuery("SELECT 1 FROM UserClass u WHERE u.username= :username").setParameter("username",username).getResultList().isEmpty());
    }

    @Transactional
    public boolean isEmailExist(String email){
        return !(entityManager.createQuery("SELECT 1 FROM UserClass u WHERE u.email= :email").setParameter("email",email).getResultList().isEmpty());
    }

    @Transactional
    public String getBirthday(String username){
        return entityManager.createQuery("SELECT u.birthday FROM UserClass u WHERE u.username = :username", String.class).setParameter("username",username).getSingleResult();
    }

    @Transactional
    public void setBirthday(String username, String date){
        UserClass user = getUserByUsername(username);
        user.setBirthday(date);
        entityManager.merge(user);
    }

    @Transactional
    public String getPassword(String username){
        return entityManager.createQuery("SELECT u.password FROM UserClass u WHERE u.username= :username", String.class).setParameter("username",username).getSingleResult();
    }

    @Transactional
    public void changePassword(String username, String password){
        UserClass user = getUserByUsername(username);
        user.setPassword(password);
    }

    // News
    @Transactional
    public void saveNews(News news){
        // Check is news exist
        if(entityManager.createQuery("SELECT n FROM News n WHERE n.title =:title").setParameter("title",news.getTitle()).getResultList().isEmpty()) {
            entityManager.persist(news);
//            entityManager.flush();
        }
    }

//    @Transactional(readOnly = true)
//    public List<News> getNewsByUsername(String username){
//        return entityManager.createQuery("SELECT n FROM News n JOIN n.author a WHERE a.username = :username", News.class).setParameter("username",username).getResultList();
//    }

    @Transactional(readOnly = true)
    public List<NewsDto> getNews(){
        return entityManager.createQuery("SELECT n.date,n.title,n.content, n.author.username FROM News n ORDER BY date DESC", NewsDto.class).setMaxResults(20).getResultList();
    }

    @Transactional(readOnly = true)
    public List<NewsDto> getNewsByPage(int page){
        if (page < 1) page = 1;
        int offset = (page - 1) * 15;
        return entityManager.createQuery("SELECT n.date,n.title,n.content, n.author.username FROM News n ORDER BY date DESC",NewsDto.class).setFirstResult(offset).setMaxResults(15).getResultList();
    }

    @Transactional
    public boolean isTitleNewsExist(String title){
        return !(entityManager.createQuery("SELECT 1 FROM News n WHERE n.title= :title").setParameter("title",title).getResultList().isEmpty());
    }

    @Transactional
    public List<NewsDto> getUserNews(String username) {
        return entityManager.createQuery("SELECT n.date,n.title,n.content,n.author.username FROM News n WHERE n.author.username = :username ORDER BY date DESC", NewsDto.class).setParameter("username",username).getResultList();

    }

    @Transactional
    public List<NewsDto> getUserNewsByPage(String username, int page) {
        if (page < 1) page = 1;
        int offset = (page - 1) * 15;
        return entityManager.createQuery("SELECT n.date,n.title,n.content,n.author.username FROM News n WHERE n.author.username = :username ORDER BY date DESC", NewsDto.class).setFirstResult(offset).setMaxResults(15).setParameter("username",username).getResultList();
    }

    @Transactional
    public Long getNewsPages(){
        Long pages = entityManager.createQuery("SELECT count(n) FROM News n",Long.class).getSingleResult();
        return pages/15;
    }
}