package com.jinx.springbootinit.constant;

public interface RabbitMqConstant {
    //死信队列命
    String DEAD_EXCHANGE_NAME = "x-dead-letter-exchange";
    //死信队列routingKey
    String DEAD_ROUTING_KEY = "x-dead-letter-routing-key";
    //队列长度
    String QUEUE_MAX_LENGTH = "x-max-length";
    //队列消息过期时间
    String QUEUE_MESSAGE_TTL = "x-message-ttl";
    //备份交换机名
    String EXCHANGE_BACKUPS_NAME = "alternate-exchange";
    //自定义交换机类型为延时交换机
    String EXCHANGE_DELAYED_MESSAGE= "x-delayed-message";
    //延时交换机类型
    String EXCHANGE_DELAYED_TYPE= "x-delayed-type";
    //优先级队列0~255
    String QUEUE_MAX_PRIORITY = "x-max-priority";
    //队列模式default和lazy
    String QUEUE_MODE = "x-queue-mode";

    String BI_EXCHANGE_NAME = "bi-exchange";

    String BI_EXCHANGE_TYPE = "direct";

    String BI_QUEUE_NAME = "bi-queue";
    String BI_ROUTING_KEY = "bi-key";
}
