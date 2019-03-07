package com.example.ueditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class UeditorApplication  extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(UeditorApplication.class);
    static int port;

    @Value("${server.port}")
    public void setPort(int port) {
        UeditorApplication.port = port;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UeditorApplication.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(UeditorApplication.class, args);
        logger.info("http://localhost:" + port);
    }

    @GetMapping("/")
    public String index() {
        return "login";
    }
}
