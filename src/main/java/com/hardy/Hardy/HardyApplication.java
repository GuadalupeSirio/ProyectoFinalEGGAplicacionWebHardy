package com.hardy.Hardy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //Anotacion para que guarde de forma automatica la fecha y hora de Cliente
public class HardyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HardyApplication.class, args);
    }

}
