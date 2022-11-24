package com.ssm.springbootrabbitmq.controller;

import com.ssm.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author shaoshao
 * @Date 2022/11/24 19:05
 * @Description: 生产者  发送消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @Description: 发送消息
     * @Date: 2022/11/24 19:09
     * @Param: [message]
     * @Return: void
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        rabbitTemplate.convertAndSend("X", "XA", "来自消息ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "来自消息ttl为40s的队列：" + message);
        log.info("{},SendMessageController.sendMsg业务结束,结果是: {}", new Date().toString(), message);

    }

    /**
     * @Description: 发送消息 设置TTL
     * @Date: 2022/11/24 20:17
     * @Param: [message, ttlTime]
     * @Return: void
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            // 设置发送消息时的延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
        log.info("{},SendMessageController.sendMsg业务结束,发送一条  {}  msTTL信息给队列QC: {}", new Date().toString(), ttlTime, message);
    }
    /**
     * @Description: 基于延迟插件 发送消息
     * @Date: 2022/11/24 22:11
     * @Param: [message, delayTime]
     * @Return: void
     */
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
            // 设置发送消息时的延迟时长
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
        log.info("{},SendMessageController.sendMsg业务结束,发送一条  {}  msTTL信息给队列QC: {}", new Date().toString(), delayTime, message);
    }
}
