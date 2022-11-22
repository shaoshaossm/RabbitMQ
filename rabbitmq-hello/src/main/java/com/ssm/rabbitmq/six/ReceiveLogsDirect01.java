package com.ssm.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

/**
 * @author shaoshao
 * @Date 2022/11/22 19:56
 * @Description:
 */
public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_DIRECT_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_DIRECT_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("disk", false, false, false, null);
        channel.queueBind("disk", EXCHANGE_DIRECT_NAME, "error");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01" + new String(message.getBody()));
        };

        channel.basicConsume("disk", true, deliverCallback, consumerTag -> {
        });
    }
}
