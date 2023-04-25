package com.project.travel;

import com.project.travel.services.FilesStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class TravelWeb implements CommandLineRunner {
	@Resource
	FilesStorageService storageService;
	public static void main(String[] args) {
		SpringApplication.run(TravelWeb.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		storageService.init();
	}
}
