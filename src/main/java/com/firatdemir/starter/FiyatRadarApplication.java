package com.firatdemir.starter;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;




@EntityScan(basePackages = { "com.firatdemir" })

@ComponentScan(basePackages = { "com.firatdemir" })
@EnableJpaRepositories(basePackages = { "com.firatdemir" })
@SpringBootApplication(scanBasePackages = "com.firatdemir")
public class FiyatRadarApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiyatRadarApplication.class, args);
	}

}
