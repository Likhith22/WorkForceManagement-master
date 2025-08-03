package com.railse.hiring.workforcemgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WorkforcemgtApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkforcemgtApplication.class, args);
	}

}
