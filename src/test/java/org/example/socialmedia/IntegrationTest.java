package org.example.socialmedia;

import org.example.socialmedia.classes.db.Dao;
import org.example.socialmedia.classes.db.UserClass;
import org.example.socialmedia.classes.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class IntegrationTest {
    @Autowired
    Dao dao;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    @Test
    void smoke_contextLoads() {
        assertNotNull(dao, "Dao is not available");
        assertNotNull(passwordEncoder, "PasswordEncoder is not available");
        assertNotNull(jwtService, "JwtService is not available");
    }


    @Test
    @Transactional
    void DaoUserTest(){
        String username = "testUsername";
        String password = passwordEncoder.encode("testPassword");
        UserClass testUser = new UserClass(username, "testemail@test.com", password, "ROLE_USER","1991-12-12");
        dao.saveUser(testUser);

        assertEquals(testUser, dao.getUserByUsername(username), "Expected user from dao doesn't match the actual user");

        String newPassword = "newTest";
        dao.changePassword(username, passwordEncoder.encode(newPassword));

        assertTrue(passwordEncoder.matches(newPassword, dao.getPassword(username)), "Changed password doesn't match the expected value");

        assertTrue(dao.isUserExist(username), "User does not exist but expected true");

        String newBirthday = "1981-12-12";
        dao.setBirthday(username, newBirthday);

        assertEquals(newBirthday, dao.getBirthday(username), "Expected birthday doesn't match the actual value");
    }

    // TODO News Dao Test

    @Test
    void JwtServiceTest(){
        String username = "testuser";
        String token = jwtService.generateToken(username);

        assertEquals(username, jwtService.extractUsername(token), "Extracted username from token doesn't match with expected username");

        assertFalse(jwtService.isExpired(token), "Token should be valid but considered expired");
        assertTrue(jwtService.verifyToken(token, username), "Token should be verified but considered unverified");
    }
}
