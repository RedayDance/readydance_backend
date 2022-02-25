package com.readydance.backend;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableBatchProcessing
@EnableJpaAuditing //Auditing 과 관련된 bean 이용가능
@SpringBootApplication
public class BackendApplication {


	public static void main(String[] args)  {

		SpringApplication.run(BackendApplication.class, args);

	}
}
