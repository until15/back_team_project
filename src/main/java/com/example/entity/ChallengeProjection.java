package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

// 첼린지 조회에 필요한 항목만
public interface ChallengeProjection {

	// 첼린지 번호
	Long getChgno();
	
	// 회원 아이디
	@Value("#{target.memberchg.memail}")
	String getMemberchgMemail();
	
}
