package com.bjpowernode.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    public static void main(String[] args) {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.174.131");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");
        Connection connection = null; //定义连接
        Channel channel = null; //定义通道

        try {
            connection = connectionFactory.newConnection();//获取连接
            channel = connection.createChannel();//获取通道
            /**
             * 声明一个队列
             *
             */
            channel.queueDeclare("myQueue",true,false,false,null);

            String message = "我的RabbitMQ的测试消息";
            /**
             * 发送消息到mq
             * 参数一位交换机  这里为空字符串表示不适使用交换机
             *
             */
           channel.basicPublish("","myQueue",null,message.getBytes("utf-8"));
            System.out.println("消息发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
