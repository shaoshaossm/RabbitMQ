package com.ssm.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author shaoshao
 * @version 1.0
 * @date 2022/11/20 16:58
 * 消费者：接收消息
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.174.131");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("hxl151359");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();
        // 声明接收消息
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println(new String(message.getBody()));
        };
        // 取消消息时回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };

        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true：代表自动应答 false:代表手动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }
}
