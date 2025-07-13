package org.example.socialmedia.classes.brockers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MqService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public String generateSecretCode(String to){
        Random random = new Random();
        // Generating secret code
        String secretCode = String.valueOf(random.nextInt(100000,999999));
        // String to, String secretCode
        MqDto data = new MqDto(to,secretCode);
        // Generating request for sending message to email
        rabbitTemplate.convertAndSend("email-exchange", "to.emailService", data);
        return secretCode;
    }
}
