package com.ssm.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

/**
 * @author shaoshao
 * @Date 2022/11/23 19:11
 * @Description: 死信队列之生产者
 */
public class Product {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 死信队列 设置TTL时间 10s
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i = 0; i < 11; i++) {
            String message = "info" + i;
//            channel.basicPublish(NORMAL_EXCHANGE, "zhangSan", properties, message.getBytes());
            channel.basicPublish(NORMAL_EXCHANGE, "zhangSan", null, message.getBytes());
        }
    }
}
