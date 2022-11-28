package com.ssm.springbootrabbitmq.consumer;

import com.ssm.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author shaoshao
 * @Date 2022/11/27 11:02
 * @Description: 发布高级 接收消息
 */
@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) {
        System.out.println(new String(message.getBody()));
        log.info("Consumer.receiveConfirmMessage业务结束,接收到的消息: {}", new String(message.getBody()));

    }
}
