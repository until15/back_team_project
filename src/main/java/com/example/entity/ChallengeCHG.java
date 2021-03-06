package com.example.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

//첼린지 인증 엔티티
@Data
@Entity
@Table(name = "CHALLENGECHG")
@SequenceGenerator(name = "SEQ_CHG_NO", sequenceName = "SEQ_CHG_NO", allocationSize = 1, initialValue = 1)

public class ChallengeCHG {

	// 챌린지번호
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHG_NO")
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
	// 1 => 대기중
	// 2 => 시작(진행중)
	// 3 => 종료
	private int recstate = 1;

	// 인원수
	private Long chgcnt = 1L;

	// 참여비
	private Long chgfee;

	// 이미지
	@Lob
	private byte[] chgimage;

	// 이미지크기
	private Long chgisize;

	// 이미지타입
	private String chgitype;

	// 이미지명
	private String chginame;

	// 생성일자
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
	@CreationTimestamp // CURRENT_DATE
	@Column(name = "CHGREGDATE")
	private LocalDate chgregdate;

	// 좋아요개수저장
	private Long chglike = 0L;

	// 난이도
	private Long chglevel = 1L;

	// 루틴
	private Long chgroutine;

	// 첼린지 기간 일 수
	private Long chgdaycnt = 1L;

	// 첼린지 생성한 사람
	@ManyToOne
	@JoinColumn(name = "memail")
	private MemberCHG memberchg;

	// ---- vue에서 string to timestamp로 변환하기 위한 임시변수 ----
	@Transient
	private String chgstart1;

	@Transient
	private String chgend1;

	@Transient
	private String recruitstart1;

	@Transient
	private String recruitend1;

}
