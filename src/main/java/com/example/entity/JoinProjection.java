package com.example.entity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

// 참가에 필요한 정보
public interface JoinProjection {

	// 참여번호
	Long getJno();
	
	// 참가 상태
	Integer getChgstate();
	
	// 참여일자
	Date getJregdate();
	
	// 달성률
	Float getChgrate();
	
	// 첼린지 번호
	@Value("#{target.challengechg.chgno}")
	Long getChallengechgChgno();	
	
	// 첼린지 제목
	@Value("#{target.challengechg.chgtitle}")
	String getChallengechgChgtitle();
	
	// 참가자 이메일
	@Value("#{target.memberchg.memail}")
	String getMemberchgMemail();

	
}
