package com.example.shrink_me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ShrinkMeApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ShrinkMeApplication.class, args);
	}
}