package com.example.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

// 첼린지 조회에 필요한 항목만
public interface ChallengeProjection {

	// 첼린지 번호
	Long getChgno();

	String getChgtitle();

	String getChgintro();

	String getChgcontent();

	Timestamp getChgstart();

	Timestamp getChgend();

	Timestamp getRecruitstart();

	Timestamp getRecruitend();

	int getRecstate();

	Long getChgcnt();

	Long getChgfee();

	Long getChglevel();

	Long getChglike();

	LocalDate getChgregdate();

	// 회원 아이디
	@Value("#{target.memberchg.memail}")
	String getMemberchgMemail();

}
