package com.joe.delayedmqdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DelayedMqDemoApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {
    }


    @Test
    public void sendDelayedMessage() throws InterruptedException {
//        MessagePostProcessor processor = new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                message.getMessageProperties().setExpiration("10000");
////                message.getMessageProperties().setTimestamp();
//                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//                return message;
//            }
//        };

        for (int i = 0; i < 5; i++) {
            Thread.sleep(10000);
//            rabbitTemplate.convertAndSend("dlx_exchange", "dlx-routing-key", ("hello world" + LocalDateTime.now().toString()));
            rabbitTemplate.convertAndSend("dlx_queue", ("hello world" + LocalDateTime.now().toString()));
        }

        System.out.println("---> Send hello world to queue");

//        rabbitTemplate.convertAndSend("sms", "lalala");
    }

}
