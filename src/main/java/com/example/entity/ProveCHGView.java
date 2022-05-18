package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "PROVECHG_VIEW")
public class ProveCHGView {

	// 인증 번호
	@Id
    @Column(name = "CFNO") // 컬럼명
	private Long cfno;
	
	// 인증 내용
	private String cfcomment;
	
	// 인증자명
	private String memail;
	
	// 성공 유무
	private int cfsuccess;
	
	// 인증일
	private Date ccregdate;
	
	// 참가번호
	private Long jno;
	
	// 참가 상태
	private int chgstate;
	
	// 첼린지 번호
	private Long chgno;
	
	// 달성률
	private float chgrate;
	
	// 첼린지 제목
	private String chgtitle;
	
	
}
