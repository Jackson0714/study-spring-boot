package com.jackson0714.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "com.jackson0714.springboot.mapper")
@SpringBootApplication
public class Springboot07DataMybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot07DataMybatisApplication.class, args);
    }
}
