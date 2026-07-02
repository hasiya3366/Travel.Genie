package com.example.TravelApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.TravelApp"})
public class TravelAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(TravelAppApplication.class, args);
    }
}
