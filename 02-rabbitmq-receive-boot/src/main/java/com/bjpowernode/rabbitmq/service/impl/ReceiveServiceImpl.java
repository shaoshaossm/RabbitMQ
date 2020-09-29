package com.bjpowernode.rabbitmq.service.impl;

import com.bjpowernode.rabbitmq.service.ReceiveService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("receiveService")
public class ReceiveServiceImpl implements ReceiveService {
    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * 这个方法不是不间断接收 不建议使用
     */
    @Override
    public void receive() {
        String message = (String) amqpTemplate.receiveAndConvert("bootQueue");
        System.out.println(message);
    }

    /**
     * @RabbitListener 用于标记当前方法是一个rabbitMQ消息监听方法 作用: 持续性的自动接收消息
     * 这个方法不需要手动调用 spring会自动运行这个监听
     * @param message 接收到的具体的消息数据
     */
    @Override
    @RabbitListener(queues = {"bootQueue"})
    public void directReceive(String message){
        System.out.println("监听器接收的消息  "+message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(),
                    exchange = @Exchange(name = "fanoutExchange",type = "fanout"))})

    public void fanoutReceive01(String message){
        System.out.println("fanoutReceive01监听器接收的消息  "+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(),
                    exchange = @Exchange(name = "fanoutExchange",type = "fanout"))})

    public void fanoutReceive02(String message){
        System.out.println("fanoutReceive02监听器接收的消息  "+message);
    }


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("topic01"),key = {"aa"},
                    exchange = @Exchange(name = "topicExchange",type = "topic")
            )
    })
    public void topicReceive01(String message){
        System.out.println("topic01消费者 ---aa---"+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("topic02"),key = {"aa.*"},
                    exchange = @Exchange(name = "topicExchange",type = "topic")
            )
    })
    public void topicReceive02(String message){
        System.out.println("topic02消费者 ---aa.*---"+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("topic03"),key = {"aa.#"},
                    exchange = @Exchange(name = "topicExchange",type = "topic")
            )
    })
    public void topicReceive03(String message){
        System.out.println("topic03消费者 ---aa.#---"+message);
    }
}
