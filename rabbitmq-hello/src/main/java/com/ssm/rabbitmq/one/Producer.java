package com.ssm.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author shaoshao
 * @version 1.0
 * @date 2022/11/20 16:22
 * 生产者：发消息
 */
public class Producer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.174.131");

        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("hxl151359");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1. 队列名称
         * 2. 队列里面消息是否持久化（磁盘）默认情况消息存储在内存中
         * 3. 改队列是否只提供一个消费者，是否消息共享，true可以多个消费者，false只能一个消费者
         * 4. 是否自动删除，最后一个消费者端断开连接以后，该队列是否自动删除，true:自动删除 false：不自动删除
         * 5. 其它参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 发送消息
        String message = "hello world";
        /**
         * 发送一个消息
         * 1. 发送到哪个交换机
         * 2. 路由的key是哪个，本次队列的名称
         * 3. 其他参数消息
         * 4. 发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");
    }

}
