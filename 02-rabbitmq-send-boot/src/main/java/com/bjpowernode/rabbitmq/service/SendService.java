package com.bjpowernode.rabbitmq.service;

public interface SendService {
    void sendMessage(String message);
    void sendFanoutMessage(String message);
    void sendtopicMessage(String message);
}
