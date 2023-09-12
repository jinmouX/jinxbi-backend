package com.jinx.springbootinit.bizmq;

import com.jinx.springbootinit.constant.RabbitMqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BiProvider {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMqConstant.BI_EXCHANGE_NAME, RabbitMqConstant.BI_ROUTING_KEY, message);
    }
}
