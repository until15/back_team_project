package com.example.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "CHG_VIEW")
public class ChallengeCHGView {
    
    // 참가 번호
	@Id
    @Column(name = "CHGNO") // 컬럼명
	private Long chgno;
			
	// 제목
	private String chgtitle;

    // 소개
	private String chgintro;
	  
	// 내용
	@Lob
	private String chgcontent;

    // 모집 시작
	@Column(nullable = false)
	private Timestamp recruitstart;
	  
	// 모집 마감
	@Column(nullable = false)
	private Timestamp recruitend;
	
	// 챌린지 시작
	@Column(nullable = false)
	private Timestamp chgstart;
	
	// 챌린지 종료
	@Column(nullable = false)
	private Timestamp chgend;

    // 모집 상태
	private int recstate = 1;
	
	// 인원수
	private Long chgcnt = 1L;
	
	// 좋아요개수저장
	private Long chglike = 0L;
	  
	// 난이도
	private Long chglevel = 1L;

	// 이미지 URL 임시변수
	@Transient
	private String imgurl;

    // 생성일자
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
	@CreationTimestamp	// CURRENT_DATE
	@Column(name = "CHGREGDATE")
	private Date chgregdate;
    
}
