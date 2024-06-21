package my.unishop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class UniShopApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name","application-api,application-core");
		SpringApplication.run(UniShopApplication.class, args);
	}

}
