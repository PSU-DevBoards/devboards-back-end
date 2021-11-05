package com.psu.devboards.dbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DbapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbapiApplication.class, args);
    }
}
