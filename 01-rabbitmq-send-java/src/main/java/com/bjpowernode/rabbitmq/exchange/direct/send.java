package com.bjpowernode.rabbitmq.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class send {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.174.131");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");

        Connection connection = null;
        Channel channel = null;

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            //创建队列
            channel.queueDeclare("myDirectQueue",true,false,false,null);
            /**
             * 声明一个劲交换机
             * 参数1 交换机的名称
             * 参数二  交换机的类型 direct fanout topic headers
             * 参数三 是否为持久化的交换机
             */
            channel.exchangeDeclare("directExchange","direct",true);
            /**
             * 将队列绑定到交换机
             */
            channel.queueBind("myDirectQueue","directExchange","directRoutingkey");
            String message = "direct的测试消息";
            /**
             * 发送消息到指定的队列
             */
            channel.basicPublish("directExchange","directRoutingkey",null,message.getBytes());
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
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
