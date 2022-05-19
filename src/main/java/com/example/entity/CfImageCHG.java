package com.example.entity;

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

// 인증 이미지 테이블
@Data
@Entity
@Table(name = "CfImageCHG")
@SequenceGenerator(name = "SEQ_CFIMAGE", sequenceName = "SEQ_CFIMAGE_NO", allocationSize = 1, initialValue = 1)
public class CfImageCHG {

	// 이미지 번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CFIMAGE") // 시퀀스 적용
    private Long cfimgno;
    
    // 이미지
    @Lob
    @Column(nullable = true)
    private byte[] cfimage;
    
    // 이미지 사이즈
    private Long cfimgsize = 0L;
    
    // 이미지 타입
    private String cfimgtype;
    
    // 이미지 이름
    private String cfimgname;
    
	// 생성일자
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
	@CreationTimestamp	// CURRENT_DATE
	@Column(name = "CFREGDATE")
	private Date cfregdate;
	
    // 인증 테이블
    private long cfno;
	
}
