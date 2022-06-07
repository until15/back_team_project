package com.example.back_team_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication

// 정의 변수 설정
@PropertySource("classpath:global.properties")

// 컨트롤러, 환경설정파일, 서비스
@ComponentScan(basePackages = {
		"com.example.controller",
		"com.example.restcontroller",
		"com.example.service",
		"com.example.config",
		"com.example.jwt",
		"com.example.schedule",
})

// 엔티티(jpa)
@EntityScan(basePackages = {
		"com.example.entity"
})

// 저장소(jpa)
@EnableJpaRepositories(basePackages = {
		"com.example.repository"
})

// filter
@ServletComponentScan(basePackages = {
		"com.example.filter"
})

// 스케줄러 사용
@EnableScheduling

public class BackTeamProjectApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BackTeamProjectApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BackTeamProjectApplication.class);
	}

}
