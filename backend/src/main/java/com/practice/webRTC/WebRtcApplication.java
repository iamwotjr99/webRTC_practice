package com.practice.webRTC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WebRtcApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebRtcApplication.class, args);
	}

}
