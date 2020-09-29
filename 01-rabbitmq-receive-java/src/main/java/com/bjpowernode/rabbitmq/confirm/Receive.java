package com.bjpowernode.rabbitmq.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receive {
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
            channel.queueDeclare("confirmQueue",true,false,false,null);
            channel.exchangeDeclare("directConfirmQueueExchange","direct",true);
            channel.queueBind("confirmQueue","directConfirmQueueExchange","confirmRoutingkey");

            channel.txSelect();
            /**
             * 获取监听某个队列 并且获取其中的数据
             */
            channel.basicConsume("confirmQueue",false,new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //获取当前消息是否被接收过  fales  没 true  接收过

                    boolean isRedeliver = envelope.isRedeliver();
                    //获取当前内部类中的通道
                    Channel c = this.getChannel();
                    if (!isRedeliver){
                        String message = new String(body);
                        System.out.println("消费者处理了消息---"+message);
                        //获取消息编号
                        long tag  = envelope.getDeliveryTag();
                    }else {
                        //c.basicAck(tag,true);

                    }


                    //手动确认消息 ,确认以后表示消息已成功处理 参数一 消息序号 参数二 确认小于消息序号的所有消息
                    // c.txCommit();

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
