package com.example.back_team_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

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

public class BackTeamProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackTeamProjectApplication.class, args);
	}

}
