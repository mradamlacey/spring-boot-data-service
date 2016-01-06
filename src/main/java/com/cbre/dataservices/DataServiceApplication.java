package com.cbre.dataservices;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableSwagger
@Configuration
@EnableAutoConfiguration
public class DataServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(DataServiceApplication.class, args);
    }
}
