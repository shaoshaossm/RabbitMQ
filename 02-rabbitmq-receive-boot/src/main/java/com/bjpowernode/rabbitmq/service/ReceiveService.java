package com.bjpowernode.rabbitmq.service;

public interface ReceiveService {
    void receive();

    void directReceive(String message);

}
