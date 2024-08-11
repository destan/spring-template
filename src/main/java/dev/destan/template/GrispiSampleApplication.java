package dev.destan.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Using exclude in order to remove auto generated password by spring security. Thanks: https://stackoverflow.com/a/51948296/878361
@SpringBootApplication
public class GrispiSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrispiSampleApplication.class, args);
	}

}
