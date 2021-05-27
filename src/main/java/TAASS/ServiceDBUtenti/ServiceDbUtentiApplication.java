package TAASS.ServiceDBUtenti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ServiceDbUtentiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDbUtentiApplication.class, args);
	}

}
