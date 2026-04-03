package ru.java.airport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.java.airport.config.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class AirportApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirportApplication.class, args);
	}

}
