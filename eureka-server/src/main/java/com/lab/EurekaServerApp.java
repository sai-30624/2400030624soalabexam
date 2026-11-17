package com.lab;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@SpringBootApplication @EnableEurekaServer
public class EurekaServerApp { public static void main(String[] a){ SpringApplication.run(EurekaServerApp.class,a);} }
