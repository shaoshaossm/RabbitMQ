package com.ssm.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author shaoshao
 * @Date 2022/11/20 20:12
 * @Description: 手动应答时不丢失，放回队列中重新消费
 */
public class Work03 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("c2等待处理消息等待20s");
        // 声明接收消息
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            // 睡眠1s
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("接收到的消息" + new String(message.getBody()));

            /**
             * 手动应答
             * 1. 消息的标记 tag
             * 2. 是否批量应答 false：不批量应答信道中的消息，true：批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

        };
        // 取消消息时回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消费者取消 消息消费被中断接口回调逻辑");
        };
        // 设置预取值为2
        int prefetchSize = 4;
        channel.basicQos(prefetchSize);
        // 手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);

    }
}
