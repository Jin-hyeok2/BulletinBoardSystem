package com.personal.bulletinboardsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BulletinboardsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulletinboardsystemApplication.class, args);
	}

}
