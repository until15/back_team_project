package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "CHGIMG_VIEW")
public class CHGImgView {

	// 첼린지 번호
	@Id
    @Column(name = "CHGNO") // 컬럼명
	private Long chgno;
	
	// 이미지
	@Lob
	@Column(nullable = true)
	private byte[] chgimage;
	
	// 이미지크기
	private Long chgisize;

	// 이미지타입
	private String chgitype;

	// 이미지명
	private String chginame;
	
}
