package com.dailype.dailypetask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DailypetaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailypetaskApplication.class, args);
	}

}
