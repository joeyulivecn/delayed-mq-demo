package com.joe.delayedmqdemo;

import org.springframework.amqp.core.*;
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

    @Bean
    CustomExchange monthlyMemberOrderExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("monthly_member_order_exchange", "x-delayed-message", true, false, args);
    }

    @Bean
    Queue monthlyMemberOrderQueue() {
        return new Queue("monthly_member_order_queue", true, false, false);
    }

    @Bean
    Binding monthlyMemberOrderBinding() {
        return BindingBuilder.bind(monthlyMemberOrderQueue())
                .to(monthlyMemberOrderExchange())
                .with("monthly-member-order-routing-key").noargs();
    }

    // try bind second queue to delayed message exchange
    @Bean
    Queue secondDelayedMQ() {
        return new Queue("second_queue", true, false, false);
    }

    @Bean
    Binding secondDelayedMQBinding() {
        return BindingBuilder.bind(secondDelayedMQ())
                .to(monthlyMemberOrderExchange())
                .with("second-queue-routing-key")
                .noargs();
    }

    @Bean
    Queue queueWithAck() {
        return new Queue("ack_test_queue", true, false, false);
    }

    //region DLX queue to handle failed task

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("orderStatusFanoutExchange", true, false);
    }

    @Bean
    Queue firstOrderQueue() {
        return new Queue("first_order_queue", true, false, false);
    }

    @Bean
    DirectExchange orderDLX() {
        return new DirectExchange("order-dlx", true, false);
    }

    @Bean
    Queue secondOrderQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "order-dlx");
        args.put("x-dead-letter-routing-key", "order-dlx-routing-key");
        return new Queue("second_order_queue", true, false, false, args);
    }

    @Bean
    Binding firstQueueBinding() {
        return BindingBuilder.bind(firstOrderQueue()).to(fanoutExchange());
    }

    @Bean
    Binding secondQueueBinding() {
        return BindingBuilder.bind(secondOrderQueue()).to(fanoutExchange());
    }

    @Bean
    Queue failedOrderStatusQueue() {
        return new Queue("failed_order_status_queue", true, false, false);
    }

    @Bean
    Binding failedOrderStatusQueueBinding() {
        return BindingBuilder.bind(failedOrderStatusQueue()).to(orderDLX()).with("order-dlx-routing-key");
    }

    //endregion
}
