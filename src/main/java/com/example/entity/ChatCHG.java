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

@Data
@Entity
@Table(name = "ChatCHG")
@SequenceGenerator(
    name = "SEQ_CHAT",
    sequenceName = "SEQ_CHAT_CNO",
    allocationSize = 1, initialValue = 1
)
public class ChatCHG {

    // 채팅 번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHAT")
    private Long chtno;
    
    // 채팅 내용(문자)
    @Lob
	private String chtcontent;

    // 생성일자
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
	@CreationTimestamp	// CURRENT_DATE
	@Column(name = "CHGREGDATE")
	private Date chtregdate;

    // ---------------외래키---------------

    // 챌린지 테이블
    @ManyToOne
    @JoinColumn(name = "chgno")
    private ChallengeCHG challengechg;
    
    // 회원테이블
    @ManyToOne
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;
}
