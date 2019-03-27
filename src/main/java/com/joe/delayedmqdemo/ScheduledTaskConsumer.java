package com.joe.delayedmqdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@RabbitListener(queues = "delayed_message_queue")
public class ScheduledTaskConsumer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "monthly_member_order_queue")
    public void memberOrderHandler(String content) {
        log.info("receive message: " + content);
    }

    @RabbitListener(queues = "second_queue")
    public void secondQueueHandler(String content) {
        log.info("receive message from second_queue: " + content);
    }

    @RabbitHandler
    public void handler(Object content) {
        log.info("receive message: " + content);
//        System.out.println("receive message: " + content);
    }

//    @RabbitHandler
//    public void handler(String content) {
//        System.out.println("receive message: " + content);
//    }
}
