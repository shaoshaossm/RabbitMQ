package com.ssm.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author shaoshao
 * @Date 2022/11/21 19:44
 * @Description: 发布确认模式：
 * - 单个确认
 * - 批量确认
 * - 异步批量确认
 */
public class ConfirmMessage {
    public static final int MESSAGE_COUNT = 100;

    public static void main(String[] args) throws Exception {
//        ConfirmMessage.publishMessageIndividually();
//        ConfirmMessage.publishMessageBatch();
        ConfirmMessage.publishMessageAsync();
    }

    /**
     * @Description: 单个发布消息确认
     * @Date: 2022/11/21 20:03
     * @Param: []
     * @Return: void
     */
    public static void publishMessageIndividually() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect(); // 开启发布确认
        long start = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发布成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时:" + (end - start) + "ms");
    }

    /**
     * @Description: 批量确认
     * @Date: 2022/11/21 20:07
     * @Param: []
     * @Return: void
     */
    public static void publishMessageBatch() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect(); // 开启发布确认
        long start = System.currentTimeMillis();
        //批量确认消息大小
        int batchSize = 100;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            if ((i + 1) % 100 == 0) {
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发布成功");
                }
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时:" + (end - start) + "ms");
    }

    /**
     * @Description: 异步批量确认
     * @Date: 2022/11/21 20:07
     * @Param: []
     * @Return: void
     */
    public static void publishMessageAsync() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect(); // 开启发布确认
        long start = System.currentTimeMillis();
        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况
         * 1.轻松的将序号与消息进行关联
         * 2.支持批量删除条目，只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        // 消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            // 删除确认的消息，剩下的是未确认的消息
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息" + deliveryTag);
        };
        // 消息确认失败 回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息："+message+"未确认的消息tag" + deliveryTag);
        };
        // 消息监听器 监听哪些消息成功了，哪些消息失败了
        channel.addConfirmListener(ackCallback, nackCallback);
        //异步批量确认消息大小
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);

        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步发布确认消息，耗时:" + (end - start) + "ms");
    }
}
