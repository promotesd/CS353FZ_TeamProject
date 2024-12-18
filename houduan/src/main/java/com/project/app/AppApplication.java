package com.project.app;

import com.project.app.util.SecurityUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AppApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AppApplication.class, args);
        SecurityUtil.setApp(run);
    }

}
