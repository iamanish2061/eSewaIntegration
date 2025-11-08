package com.eSewaIntegration;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ESewaIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ESewaIntegrationApplication.class, args);
	}

    @Bean
    public Gson gson(){
        return new Gson();
    }

}
