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
public class ReceiveLogsDirect02 {

    public static final String EXCHANGE_DIRECT_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_DIRECT_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("console", false, false, false, null);
        channel.queueBind("console", EXCHANGE_DIRECT_NAME, "info");
        channel.queueBind("console", EXCHANGE_DIRECT_NAME, "warning");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect02" + new String(message.getBody()));
        };

        channel.basicConsume("console", true, deliverCallback, consumerTag -> {
        });
    }
}
