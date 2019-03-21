package com.joe.delayedmqdemo;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableRabbit
@SpringBootApplication
public class DelayedMqDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelayedMqDemoApplication.class, args);
    }

}
