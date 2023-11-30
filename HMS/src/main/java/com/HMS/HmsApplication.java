package com.HMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.HMS.entity")
public class HmsApplication {
	public static void main(String[] args) {
		SpringApplication.run(HmsApplication.class, args);
	}

}
