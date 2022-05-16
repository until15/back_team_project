package com.example.entity;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public interface JoinningProjection {

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
	
	// 첼린지 참가인원
	@Value("#{target.challengechg.chgcnt}")
	Long getChallengechgChgcnt();
	
	// 첼린지 시작일
	@Value("#{target.challengechg.chgstart}")
	Timestamp getChallengechgChgstart();
	
	// 첼린지 종료일
	@Value("#{target.challengechg.chgend}")
	Timestamp getChallengechgChgend();
	
	// 참가비
	@Value("#{target.challengechg.chgfee}")
	Long getChallengechgChgfee();
	
	// 좋아요 개수
	@Value("#{target.challengechg.chglike}")
	Long getChallengechgChglike();
	
	// 첼린지 난이도
	@Value("#{target.challengechg.chglevel}")
	Long getChallengechgChglevel();
	
	// 썸네일 이미지
	@Value("#{target.challengechg.chgimage}")
	byte[] getChallengechgChgimage();
	
	// 썸네일 이름
	@Value("#{target.challengechg.chginame}")
	String getChallengechgChginame();
	
	// 썸네일 사이즈
	@Value("#{target.challengechg.chgisize}")
	Long getChallengechgChgisize();
	
	// 썸네일 타입
	@Value("#{target.challengechg.chgitype}")
	String getChallengechgChgitype();

	// 참가자 이메일
	@Value("#{target.memberchg.memail}")
	String getMemberchgMemail();
	
}
