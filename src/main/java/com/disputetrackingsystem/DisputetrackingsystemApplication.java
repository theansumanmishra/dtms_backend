package com.disputetrackingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DisputetrackingsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisputetrackingsystemApplication.class, args);
	}
}
