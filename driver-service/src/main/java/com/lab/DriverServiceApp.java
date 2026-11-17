package com.lab;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DriverServiceApp {

    private final WebServerApplicationContext ctx;
    private final EurekaInstanceConfigBean eureka;

    public DriverServiceApp(WebServerApplicationContext ctx, EurekaInstanceConfigBean eureka) {
        this.ctx = ctx;
        this.eureka = eureka;
    }

    public static void main(String[] args) {
        SpringApplication.run(DriverServiceApp.class, args);
    }

    // Each instance reports its own dynamic port, so you can SEE round robin happen
    @GetMapping("/driver/info")
    public Map<String, Object> info() {
        return Map.of(
                "message", "Response from Driver Service",
                "port", ctx.getWebServer().getPort(),
                "instanceId", eureka.getInstanceId());
    }
}
