package com.example.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "JOINONE_VIEW")
public class JoinOneView {

	// 참가 번호
	@Id
	@Column(name = "JNO")
	private Long jno;
	
	// 첼린지 상태
	private int recstate;
	
	// 진행 상태
	private int chgstate;
	
	// 참여일자
	private Date jregdate;
	
	// 첼린지 번호
	private Long chgno;
	
	// 첼린지 제목
	private String chgtitle;
	
	// 첼린지 소개글
	private String chgintro;
	
	// 첼린지 내용
	@Lob
	private String chgcontent; 
	
	// 첼린지 시작일
	private Timestamp chgstart;
	
	// 첼린지 마감일
	private Timestamp chgend;
	
	// 달성률
	private float chgrate;
	
	// 인원수
	private Long chgcnt;
	
	// 참가비
	private Long chgfee;
	
	// 난이도
	private Long chglevel;
	
	// 첼린지 좋아요 갯수
	private Long chglike;
	
	// 참가자 아이디
	private String memail;
	
	// 첼린지 생성자 아이디
	private String cid;
	
}