package com.ssm.rabbitmq.eight;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

/**
 * @author shaoshao
 * @Date 2022/11/23 19:01
 * @Description: 死信队列消费者
 */
public class Consumer02 {

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02接受的消息是：" + new String(message.getBody(), "UTF-8"));
        };

        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {
        });
    }

}
