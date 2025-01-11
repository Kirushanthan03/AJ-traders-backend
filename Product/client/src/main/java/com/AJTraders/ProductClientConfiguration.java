package com.AJTraders;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.AJTraders")
public class ProductClientConfiguration {
}