package com.example.jobsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class JobsappApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobsappApplication.class, args);
	}

}
