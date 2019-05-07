package com.dmcn.two;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dmcn.two.mapper")
public class TwoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TwoApplication.class, args);
    }

}
