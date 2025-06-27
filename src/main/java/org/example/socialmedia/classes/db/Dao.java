package org.example.socialmedia.classes.db;

import jakarta.persistence.EntityManager;
import org.example.socialmedia.classes.brockers.MqService;
import org.example.socialmedia.classes.controllers.ApiExceptionController;
import org.example.socialmedia.classes.logging.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class Dao {
    @Autowired
    EntityManager entityManager;
    @Autowired
    ApiExceptionController apiExceptionController;
    //
    @Autowired
    MqService mqService;

    // User
    @Loggable
    @Transactional
    public void saveUser(UserClass userClass){
        // Check is user exist
        if(entityManager.createQuery("SELECT u FROM UserClass u WHERE u.username= :username OR u.email= :email").setParameter("username",userClass.getUsername()).setParameter("email",userClass.getEmail()).getResultList().isEmpty()) {
            entityManager.persist(userClass);
//            entityManager.flush();
        }
    }

    @Transactional(readOnly = true)
    public UserClass getUserByUsername(String username) throws UsernameNotFoundException {
        List<UserClass> result = entityManager.createQuery("SELECT u FROM UserClass u WHERE u.username = :username", UserClass.class).setParameter("username",username).getResultList();
        if(result.isEmpty()) throw new UsernameNotFoundException("User not found");
//         TODO remove
//        mqProducer.sendMessage("user gotten");
        return result.get(0);
    }

    @Transactional(readOnly = true)
    public List<UserClassDto> getUsersByPage(int page){
        if (page < 1) page = 1;
        int offset = (page - 1) * 10;
        return entityManager.createQuery("SELECT u.username, u.email, n.title, u.role FROM UserClass u LEFT JOIN u.news n", UserClassDto.class).setFirstResult(offset).setMaxResults(10).getResultList();
    }

    @Transactional(readOnly = true)
    public boolean isUserExist(String username){
        return !(entityManager.createQuery("SELECT 1 FROM UserClass u WHERE u.username= :username").setParameter("username",username).getResultList().isEmpty());
    }

    @Transactional(readOnly = true)
    public boolean isEmailExist(String email){
        return !(entityManager.createQuery("SELECT 1 FROM UserClass u WHERE u.email= :email").setParameter("email",email).getResultList().isEmpty());
    }

    @Transactional(readOnly = true)
    public String getEmail(String username){
        return entityManager.createQuery("SELECT u.email FROM UserClass u WHERE u.username = :username", String.class).setParameter("username",username).getSingleResult();
    }

    @Transactional(readOnly = true)
    public String getBirthday(String username){
        return entityManager.createQuery("SELECT u.birthday FROM UserClass u WHERE u.username = :username", String.class).setParameter("username",username).getSingleResult();
    }

    @Transactional
    public void setBirthday(String username, String date){
        UserClass user = getUserByUsername(username);
        user.setBirthday(date);
        entityManager.merge(user);
    }

    @Transactional(readOnly = true)
    public String getPassword(String username){
        return entityManager.createQuery("SELECT u.password FROM UserClass u WHERE u.username= :username", String.class).setParameter("username",username).getSingleResult();
    }

    @Loggable
    @Transactional
    public void changePassword(String username, String password){
        UserClass user = getUserByUsername(username);
        user.setPassword(password);
    }


    // TODO
//    @Loggable
//    @Transactional
//    public void verifyEmail(String username){
//        UserClass user = getUserByUsername(username);
//        user.verifyEmail();
//    }

    @Transactional(readOnly = true)
    public Boolean isEmailVerified(String username){
        return entityManager.createQuery("SELECT u.isEmailVerified from UserClass u WHERE u.username = :username", Boolean.class).setParameter("username",username).getSingleResult();
    }

    @Transactional
    public void verifyEmail(String username){
        UserClass user = getUserByUsername(username);
        String email = user.getEmail();

        EmailVerification verif = entityManager.createQuery("SELECT ev FROM EmailVerification ev WHERE ev.user.username = :username", EmailVerification.class).setParameter("username",username).getSingleResult();

        if(verif.getExpiresAt() != null && verif.getExpiresAt().before(new Date())){
            // Do nothing
            return;
        }

        String verificationCode = mqService.generateSecretCode(email);

        verif.setCodeHash(verificationCode);
        verif.setCreatedAt(new Date());
        verif.setExpiresAt(new Date(System.currentTimeMillis()+1000*60*20));

        entityManager.merge(verif);
    }

    @Transactional
    public boolean verifyEmailByCode(String username, String code){
        UserClass user = getUserByUsername(username);
        EmailVerification verif = entityManager.createQuery("SELECT ev FROM EmailVerification ev WHERE ev.user.username = :username", EmailVerification.class).setParameter("username",username).getSingleResult();

        if(verif.getExpiresAt().before(new Date())){
            return false;
        }
        if(verif.getCodeHash().equals(code)){
            verif.setVerified(true);
            user.setEmailVerification(verif);
            user.setEmailVerified(true);
            entityManager.merge(user);
            return true;
        }
        return false;
    }

    // News
    @Loggable
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

    @Transactional(readOnly = true)
    public boolean isTitleNewsExist(String title){
        return !(entityManager.createQuery("SELECT 1 FROM News n WHERE n.title= :title").setParameter("title",title).getResultList().isEmpty());
    }

    @Transactional(readOnly = true)
    public List<NewsDto> getUserNews(String username) {
        return entityManager.createQuery("SELECT n.date,n.title,n.content,n.author.username FROM News n WHERE n.author.username = :username ORDER BY date DESC", NewsDto.class).setParameter("username",username).getResultList();

    }

    @Transactional(readOnly = true)
    public List<NewsDto> getUserNewsByPage(String username, int page) {
        if (page < 1) page = 1;
        int offset = (page - 1) * 15;
        return entityManager.createQuery("SELECT n.date,n.title,n.content,n.author.username FROM News n WHERE n.author.username = :username ORDER BY date DESC", NewsDto.class).setFirstResult(offset).setMaxResults(15).setParameter("username",username).getResultList();
    }

    @Transactional(readOnly = true)
    public Long getNewsPages(){
        Long pages = entityManager.createQuery("SELECT count(n) FROM News n",Long.class).getSingleResult();
        if (pages<15) return 1L;
        return pages/15;
    }
}