package com.example.springWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.springWeb")
public class SpringWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebApplication.class, args);
	}

}
