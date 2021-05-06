package com.emailAddressGenerator;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class EmailAddressGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailAddressGeneratorApplication.class, args);
    }
}
