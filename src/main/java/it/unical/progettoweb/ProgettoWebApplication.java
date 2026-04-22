package it.unical.progettoweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ProgettoWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgettoWebApplication.class, args);
    }

}
