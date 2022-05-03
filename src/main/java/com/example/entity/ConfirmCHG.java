package com.example.entity;

import java.util.Date;

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

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// 인증 테이블
@Data
@Entity
@Table(name = "ConfirmCHG") // 테이블명
//생성할 시퀀스
@SequenceGenerator(name = "SEQ_CFM", sequenceName = "SEQ_CFM_CNO", allocationSize = 1, initialValue = 1)
public class ConfirmCHG {

	// 인증번호
	@Id
    @Column(name = "CFNO") // 컬럼명
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CFM") // 시퀀스 적용
	private Long cfno;

	// 인증글
	@Lob
	@Column(nullable = true)
	private String cfcomment;
	
	// 성공유무
	private int cfsuccess;

	// 인증일
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
	@CreationTimestamp	// CURRENT_DATE
	@Column(name = "CCREGDATE")
	private Date ccregdate;

	// 작성자
    @ManyToOne
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;
	
	// 참가 테이블
	@ManyToOne
	@JoinColumn(name = "jno")
	private JoinCHG joinchg;
	
}
