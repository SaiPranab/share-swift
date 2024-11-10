package com.shareswift;

import com.shareswift.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class ShareSwiftApplication {

	private final FileService service;
	
	public static void main(String[] args) {
		SpringApplication.run(ShareSwiftApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return (String ...args) -> {
				service.createDIerctory();
		};
	}
}
