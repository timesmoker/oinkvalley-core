package com.oinkvalley.oinkvalleycore;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OinkvalleyCoreApplication {

    public static void main(String[] args) {

        boolean envLoggingEnabled = Boolean.parseBoolean(System.getenv().getOrDefault("ENVLOG_ENABLED", "true"));


            System.out.println("====================================");
            System.out.println("✅ 현재 시간: " +
                    ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));
        if (envLoggingEnabled) {
            System.out.println("📦 EnvLogger - 주입된 환경변수 확인");
            System.out.println("IS DEVmod? :" + System.getenv("DEV_MODE"));
            System.out.println("✅ JWT_SECRET: " + System.getenv().getOrDefault("JWT_SECRET", "NOT_SET"));
            System.out.println("✅ DB URL: " + System.getenv().getOrDefault("SPRING_DATASOURCE_URL", "NOT_SET"));
            System.out.println("✅ DB Username: " + System.getenv().getOrDefault("SPRING_DATASOURCE_USERNAME", "NOT_SET"));
            String pw = System.getenv().get("SPRING_DATASOURCE_PASSWORD");
            System.out.println("✅ DB Password: " + pw);
        }
            System.out.println("====================================");

        SpringApplication.run(OinkvalleyCoreApplication.class, args);
    }

}
