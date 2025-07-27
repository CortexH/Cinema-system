package com.example.scheduling_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchedulingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(SchedulingServiceApplication.class, args);
	}
	// Só agora eu vi o que colocaram aqui. Decepcionante, sinceramente.
	// Nota mental: NUNCA DEIXE O COMPUTADOR LIGADO EM LUGARES COM PESSOAS QUE PODEM ESTRAGAR AS SUAS COISAS.
}
