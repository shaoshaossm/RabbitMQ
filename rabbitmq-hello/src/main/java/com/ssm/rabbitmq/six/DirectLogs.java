package com.ssm.rabbitmq.six;

import com.rabbitmq.client.Channel;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * @author shaoshao
 * @Date 2022/11/22 19:34
 * @Description:
 */
public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
