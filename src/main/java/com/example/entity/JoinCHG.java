package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

//참가 테이블
@Data
@Entity
@Table(name = "JoinCHG") // 테이블명
//생성할 시퀀스
@SequenceGenerator(name = "SEQ_JOIN", sequenceName = "SEQ_JOIN_JNO", allocationSize = 1, initialValue = 1)
public class JoinCHG {

    // 참여번호
    @Id
    @Column(name = "JNO") // 컬럼명
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JOIN") // 시퀀스 적용
    private Long jno;
    
    // 참가 상태
    // 1 => 참가
    // 2 => 포기
    private int chgstate = 1;
    
	// 생성일자
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
	@CreationTimestamp	// CURRENT_DATE
	@Column(name = "JREGDATE")
	private Date jregdate;

    // 챌린지 테이블
    @ManyToOne
    @JoinColumn(name = "chgno")
    private ChallengeCHG challengechg;
    
    // 회원테이블
    @ManyToOne
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;
	
}
