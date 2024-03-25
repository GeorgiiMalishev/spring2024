package my.spring2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"my.spring2024.app", "my.spring2024.domain", "my.spring2024.infrastructure"})
public class Spring2024Application {

	public static void main(String[] args) {
		SpringApplication.run(Spring2024Application.class, args);
	}

}
