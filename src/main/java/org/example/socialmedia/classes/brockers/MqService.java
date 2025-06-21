package org.example.socialmedia.classes.brockers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MqService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    // TODO sender
    public String acceptVerify(String to){
        Random random = new Random();
        // Generating secret code
        String secretCode = String.valueOf(random.nextInt(10000,99999));
        // String to, String secretCode
        MqDto data = new MqDto(to,secretCode);

        rabbitTemplate.convertAndSend("email-exchange", "to.emailService", data);
        return secretCode;
    }
}
