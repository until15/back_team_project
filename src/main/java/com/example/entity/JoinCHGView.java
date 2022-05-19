package com.example.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "JOINCHG_VIEW")
public class JoinCHGView {

	// 참가 번호
	@Id
    @Column(name = "JNO") // 컬럼명
	private Long jno;
	
	// 참가 상태
	private int chgstate = 1;
	
	// 달성률
	private float chgrate = 0;
	
	// 참여일자
	private Date jregdate;
	
	// 참가자명
	private String memail;
	
	// 첼린지 번호
	private Long chgno;
	
	// 제목
	private String chgtitle;
	
	// 챌린지 시작
	@Column(nullable = false)
	private Timestamp chgstart;
	
	// 챌린지 종료
	@Column(nullable = false)
	private Timestamp chgend;
	
	// 인원수
	private Long chgcnt = 1L;
	
	// 좋아요개수저장
	private Long chglike = 0L;
	  
	// 난이도
	private Long chglevel = 1L;
	
	// 이미지
	@Transient
	private String jimgurl;
	
}
