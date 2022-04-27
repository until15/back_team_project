package com.example.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

//첼린지 인증 엔티티
@Data
@Entity
@Table(name = "ChallengeCHG")
@SequenceGenerator(name="SEQ_CHG_NO",
		sequenceName = "SEQ_CHG_NO", 
		allocationSize = 1, 
		initialValue = 1)
public class ChallengeCHG {

	// 챌린지번호
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
		generator = "SEQ_CHG_NO")
	private Long chgno;
	  
	// 제목
	private String chgtitle;
	  
	// 소개
	private String chgintro;
	  
	// 내용
	@Lob
	private String chgcontent;
	  
	// 챌린지 시작
	@Column(nullable = false)
	private Timestamp chgstart;
	  
	// 챌린지 종료
	@Column(nullable = false)
	private Timestamp chgend;
	  
	// 모집 시작
	@Column(nullable = false)
	private Timestamp recruitstart;
	  
	// 모집 마감
	@Column(nullable = false)
	private Timestamp recruitend;
	
	// 모집 상태
	private String recstate;
	  
	// 인원수
	private Long chgcnt;
	  
	// 참여비
	private Long chgfee;
	  
	// 생성일자
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
	@CreationTimestamp	// CURRENT_DATE
	@Column(name = "CHGREGDATE")
	private Date chgregdate;
	  
	// 좋아요개수저장
	private Long chglike;
	  
	// 난이도
	private Long chglevel = 1L;
	
}
