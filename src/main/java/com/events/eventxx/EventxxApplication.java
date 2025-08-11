package com.events.eventxx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EventxxApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventxxApplication.class, args);
	}

}
