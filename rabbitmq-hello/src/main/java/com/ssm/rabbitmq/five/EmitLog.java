package com.ssm.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

import java.sql.SQLOutput;
import java.util.Scanner;

/**
 * @author shaoshao
 * @Date 2022/11/22 19:34
 * @Description:
 */
public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 声明交换机 消费者声明了这里可以不声明
//        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
