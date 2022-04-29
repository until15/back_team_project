package com.example.entity;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public interface JoinSelectOne {

	// 참여번호
	Long getJno();
	
	// 참가 상태
	Integer getChgstate();
	
	// 참여일자
	Date getJregdate();
	
	// 첼린지 번호
	@Value("#{target.challengechg.chgno}")
	Long getChallengechgChgno();	
	
	// 첼린지 제목
	@Value("#{target.challengechg.chgtitle}")
	String getChallengechgChgtitle();
	
	// 첼린지 시작일
	@Value("#{target.challengechg.chgstart}")
	Timestamp getChgstart();
	
	// 첼린지 종료일
	@Value("#{target.challengechg.chgend}")
	Timestamp getChgend();
	
	// 달성률
	Float getChgrate();
	
	// 인원수
	@Value("#{target.challengechg.chgcnt}")
	Long getChgcnt();
	
	// 참가비
	@Value("#{target.challengechg.chgfee}")
	Long getChgfee();
	
	// 난이도
	@Value("#{target.challengechg.chglevel}")
	Long getChglevel();
	
	// 좋아요 갯수
	@Value("#{target.challengechg.chglike}")
	Long getChglike();
	
	// 참가자 이메일
	@Value("#{target.memberchg.memail}")
	String getMemberchgMemail();
	
}
