package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

//회원테이블
@Data
@Entity
@Table(name = "MemberCHG")
public class MemberCHG {

	// 이메일
	@Id
	private String memail;

	// 닉네임
	@Column(nullable = false)
	private String mid;

	// 암호
	@Column(nullable = false)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String mpw;

	// 이름
	@Column(nullable = false)
	private String mname;

	// 생년월일
	private String mbirth;

	// 성별
	private Long mgender;

	// 키
	private Long mheight;

	// 몸무게
	private Long mweight;

	// 연락처
	private String mphone;

	// 권한
	private String mrole;

	// 등급
	private Long mrank = 1L;
	
	// 프로필 사진
	@Lob
	private byte[] mprofile;
	  
	// 프사 사이즈
	private Long mpsize = 0L;
	  
	// 프사 타입
	private String mptype;
	  
	// 프사 이름
	private String mpname;
	
	// 탈퇴 유무
	private int mstep;

	// 가입일자
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
	@CreationTimestamp // CURRENT_DATE
	@Column(name = "MREGDATE")
	private Date mregdate;
	
}