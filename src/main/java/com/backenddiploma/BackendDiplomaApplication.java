package com.backenddiploma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.backenddiploma.models")
public class BackendDiplomaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendDiplomaApplication.class, args);
    }

}
