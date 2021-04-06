package com.example.report;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;


@SpringBootApplication
public class DemoApplication {

    
    @Autowired
    UserDetailsService userDetail;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
	}
	
	// @Bean
    // CommandLineRunner initDatabase(ReportRepository repository) {

        // User admin = (User)userDetail.loadUserByUsername("admin");

        // return args -> {
        //     repository.save(new Report("First report", Report.Status.DRAFT,admin));
        //     repository.save(new Report("Second report", Report.Status.INWORK,admin));
        //     repository.save(new Report("3rd report", Report.Status.ACCEPTED,admin));
        //     repository.save(new Report("Fourth report", Report.Status.REJECTED,admin));
        // };
    // }
}