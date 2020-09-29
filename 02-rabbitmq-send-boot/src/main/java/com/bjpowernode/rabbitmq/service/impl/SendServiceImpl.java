package com.bjpowernode.rabbitmq.service.impl;

import com.bjpowernode.rabbitmq.service.SendService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("sendService")
public class SendServiceImpl implements SendService {
    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    public void sendMessage(String message) {
        /**
         * 发送消息
         * 参数一 交换机
         * 参数二 Routingkey
         * 参数三 发送的具体消息数据
         *
         */
        amqpTemplate.convertAndSend("bootDirectExchange","bootDirectRouting",message);
    }

    @Override
    public void sendFanoutMessage(String message) {
        amqpTemplate.convertAndSend("fanoutExchange","",message);
    }
    @Override
    public void sendtopicMessage(String message) {
        amqpTemplate.convertAndSend("topicExchange","aa.bb.cc",message);
    }
}
