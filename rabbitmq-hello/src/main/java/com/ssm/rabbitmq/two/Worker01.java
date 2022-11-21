package com.ssm.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author shaoshao
 * @version 1.0
 * @date 2022/11/20 18:47
 * 工作线程 相当于消费者
 */
public class Worker01 {


    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        // 声明接收消息
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("接收到的消息"+new String(message.getBody()));
        };
        // 取消消息时回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag+"消息消费被中断接口回调逻辑");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
