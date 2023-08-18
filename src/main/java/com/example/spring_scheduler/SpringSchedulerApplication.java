package com.example.spring_scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.scheduling.annotation.EnableScheduling;


@Slf4j
@EnableScheduling
@SpringBootApplication
public class SpringSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringSchedulerApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);

        ApplicationContext applicationContext = app.run(args);
        printScannedBeans(applicationContext);
        printComplete();
    }

    private static void printScannedBeans(ApplicationContext applicationContext) {
        log.info("Application run ...");
        for (String name : applicationContext.getBeanDefinitionNames()) {
            log.debug("Scanned bean: " + name);
        }
    }

    private static void printComplete() {
        log.info("Java " + Runtime.version() + ", Spring Boot " + SpringBootVersion.getVersion() + ", Spring Framework " + SpringVersion.getVersion());
        log.info("Settings Complete");
    }

}
