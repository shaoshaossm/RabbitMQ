package com.ssm.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ssm.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;

/**
 * @author shaoshao
 * @Date 2022/11/23 19:01
 * @Description: 死信队列消费者
 */
public class Consumer01 {
    // 普通和死信交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通和死信队列
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明死信和普通交换机，类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列
        HashMap<String, Object> arguments = new HashMap<>();
        // 过期时间 10s 在生产者那边声明更灵活
//         arguments.put("x-message-ttl", 10000);
        //正常的队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);//图中红箭头
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置正常队列的长度限制
//        arguments.put("x-max-length", 6);

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        //绑定普通的交换机与队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangSan");
        //绑定死信的交换机与队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(),"UTF-8");
            if (msg.equals("info5")){
                System.out.println("Consumer01接收的消息："+msg+" 此消息是被拒绝的");
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01接收的消息："+msg);
            }

        };
        // 开启手动应答 为消息被拒 false
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
        });
//        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, consumerTag -> {
//        });
    }

}
