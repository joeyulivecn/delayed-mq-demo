package com.joe.delayedmqdemo;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

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

//    @RabbitHandler
//    public void handler(Object content) {
//        log.info("receive message: " + content);
////        System.out.println("receive message: " + content);
//    }

//    @RabbitHandler
//    public void handler(String content) {
//        System.out.println("receive message: " + content);
//    }

    @RabbitListener(queues = "ack_test_queue")
    public void handlerAck(Object content) {
        log.info("receive message: " + content);
    }

    @RabbitListener(queues = "first_order_queue")
    public void handleFirstOrderStatusQueue(Object content) {
        log.info("receive message: " + content);
    }

    @RabbitListener(queues = "second_order_queue")
    public void handleSecondOrderStatusQueue(String content, Channel channel, Message message) throws IOException {
        log.info("receive message: " + message);
        if (new Random().nextInt() % 2 == 0) {
            log.debug("ack: " + content);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            log.error("nack: " + content);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
