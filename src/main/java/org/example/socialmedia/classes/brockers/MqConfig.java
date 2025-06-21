package org.example.socialmedia.classes.brockers;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
//    @Bean
//    Queue queue() {
//        return new Queue("email-queue", false);
//    }

    @Bean
    Exchange exchange(){
        return new DirectExchange("email-exchange");
    }

//    @Bean
//    Binding binder(Queue queue, Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("to.emailService").noargs();
//    }

}
