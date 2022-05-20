package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "CONFIRM_VIEW ")
public class ConfirmView {

	// 인증 번호
	@Id
	@Column(name = "CFNO")
	private Long cfno;
	
	// 인증글
	private String cfcomment;
	
	// 성공유무
	private int cfsuccess;
	
	// 작성자
	private String memail;
	
	// 참가번호
	private Long jno;
	
	// 첼린지 번호
	private Long chgno;
	
	// 달성률
	private float chgrate;
	
	// 인증일
	private Date ccregdate;
	
	
}
