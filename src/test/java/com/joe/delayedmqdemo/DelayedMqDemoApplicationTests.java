package com.joe.delayedmqdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void delayedMessageExchangePlugin() {
        MessagePostProcessor processor = message -> {
            message.getMessageProperties().setHeader("x-delay", 1000 * 30);
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        };

        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend("monthly_member_order_exchange", "monthly-member-order-routing-key", ("hello world" + LocalDateTime.now().toString()), processor);
        }

        MessagePostProcessor processor2 = message -> {
            message.getMessageProperties().setHeader("x-delay", 1000 * 15);
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        };

        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend("monthly_member_order_exchange", "second-queue-routing-key", ("to second queue" + LocalDateTime.now().toString()), processor2);
        }

        System.out.println("---> Send hello world to queue");
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
            rabbitTemplate.convertAndSend("delayed_message_exchange", ("hello world" + LocalDateTime.now().toString()));
        }

        System.out.println("---> Send hello world to queue");

//        rabbitTemplate.convertAndSend("sms", "lalala");
    }

    @Test
    public void fanoutTest() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("orderStatusFanoutExchange", "", "order status: " + i);
        }
    }
}
