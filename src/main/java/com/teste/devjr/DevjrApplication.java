package com.teste.devjr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevjrApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevjrApplication.class, args);
    }

}


