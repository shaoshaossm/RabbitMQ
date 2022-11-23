package com.ssm.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

import java.sql.SQLOutput;
import java.util.Queue;

/**
 * @author shaoshao
 * @Date 2022/11/22 20:27
 * @Description: 消费者C1
 */
public class ReceiveLogsTopic01 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare("Q1", false, false, false, null);
        channel.queueBind("Q1", EXCHANGE_NAME, "*.orange.*");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsTopic01" + new String(message.getBody(), "UTF-8"));
            System.out.println("接收队列：" + "Q1" + "绑定键：" + message.getEnvelope().getRoutingKey());

        };
        channel.basicConsume("Q1", true, deliverCallback, consumerTag -> {
        });


    }
}
