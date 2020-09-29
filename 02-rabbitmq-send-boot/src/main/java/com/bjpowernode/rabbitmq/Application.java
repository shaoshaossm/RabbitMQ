package com.bjpowernode.rabbitmq;

import com.bjpowernode.rabbitmq.service.SendService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext ac = SpringApplication.run(Application.class, args);
		SendService sendService = (SendService) ac.getBean("sendService");
		//sendService.sendMessage("Boot的测试数据");
		//sendService.sendFanoutMessage("BootFanout的测试数据");
		sendService.sendtopicMessage("BootTopic的测试数据为aa");

	}

}
