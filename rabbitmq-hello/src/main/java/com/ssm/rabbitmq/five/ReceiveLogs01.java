package com.ssm.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

/**
 * @author shaoshao
 * @Date 2022/11/22 19:20
 * @Description:
 */
public class ReceiveLogs01 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明一个临时队列 当消费者与队列断开时，队列自动删除
        String queueName = channel.queueDeclare().getQueue();
        // 绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("ReceiveLogs01"+new String(message.getBody()));
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

    }
}
