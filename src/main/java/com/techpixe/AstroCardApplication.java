package com.techpixe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.techpixe.entity")
@EnableJpaRepositories(basePackages = "com.techpixe.repository")
@EnableScheduling
@EnableTransactionManagement
public class AstroCardApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstroCardApplication.class, args);
	}

}
