package com.bjpowernode.rabbitmq;

import com.bjpowernode.rabbitmq.service.ReceiveService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext ac = SpringApplication.run(Application.class, args);
		ReceiveService service = (ReceiveService) ac.getBean("receiveService");
		//使用了消息监听器那么就不需要调用接收方法来接收消息

	//	service.receive();
	}

}
