package org.example.socialmedia.classes.db;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
    public List<UserClass> getUsersByPage(int page){
        return entityManager.createQuery("SELECT u.id,u.username,u.email,u.role from UserClass u ORDER BY u.id").setFirstResult((page-1) * 10).setMaxResults(10).getResultList();
    }
}