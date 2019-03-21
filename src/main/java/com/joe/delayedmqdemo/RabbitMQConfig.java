package com.joe.delayedmqdemo;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    @Bean("dlxExchange")
    DirectExchange dlxExchange() {
        return new DirectExchange("dlx_exchange", true, false);
    }

    @Bean("deadLetterQueue")
    Queue deadLetterQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "dlx_exchange");
        args.put("x-dead-letter-routing-key", "dlx-routing-key");
        args.put("x-message-ttl", 120000);
        return new Queue("dlx_queue", true, false, false, args);
    }

//    @Bean
//    Binding deadLetterBinding() {
//        return BindingBuilder.bind(deadLetterQueue()).to(dlxExchange()).with("dlx-routing-key");
//    }

    @Bean("delayedMessageQueue")
    Queue delayedMessageQueue() {
        return new Queue("delayed_message_queue", true, false, false);
    }

    @Bean
    Binding delayedMessageBinding() {
        return BindingBuilder.bind(delayedMessageQueue()).to(dlxExchange()).with("dlx-routing-key");
    }
}
