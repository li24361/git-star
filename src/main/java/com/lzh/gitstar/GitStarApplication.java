package com.lzh.gitstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GitStarApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitStarApplication.class, args);
	}

}
