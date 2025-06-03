package org.example.socialmedia.classes.db;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Dao {
    @Autowired
    EntityManager entityManager;

    @Transactional
    public void saveUser(UserClass userClass){
        UserClass newUser = new UserClass(userClass.getUsername(), userClass.getEmail(), userClass.getPassword(), userClass.getRole());
        entityManager.persist(newUser);
    }

    @Transactional(readOnly = true)
    public UserClass getUserByUsername(String username) {
        List<UserClass> result = entityManager.createQuery("SELECT u FROM UserClass u WHERE u.username = :username", UserClass.class).setParameter("username",username).getResultList();
        if(result.isEmpty()) throw new UsernameNotFoundException("User not found");
        return result.get(0);
    }

    @Transactional(readOnly = true)
    public boolean isUsernameExist(String username){
        Long count =  entityManager.createQuery("SELECT count(u) FROM UserClass u WHERE u.username = :username",Long.class).setParameter("username",username).getSingleResult();
        return !count.equals(0);
    }

    @Transactional(readOnly = true)
    public boolean isEmailExist(String email){
        Long count = entityManager.createQuery("SELECT count(u) FROM UserClass u WHERE u.email = :email",Long.class).setParameter("email",email).getSingleResult();
        return !count.equals(0);
    }

    @Transactional(readOnly = true)
    public List<UserClass> getUsers(){
        return entityManager.createQuery("SELECT u FROM UserClass u", UserClass.class).getResultList();
    }
}