package com.example.entity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

// 인증 조회에 필요한 항목만 담음
public interface ConfirmProjection {

	// 인증번호
	Long getCfno();
	
	// 인증글
	String getCfcomment();
	
	// 성공유무
	Integer getCfsuccess();
	
	// 작성자
	@Value("#{target.memberchg.memail}")
	String getMemberchgMemail();
	
	// 참가번호 (Join Entity)
	@Value("#{target.joinchg.jno}")
	Long getJoinchgJno();
	
	// 첼린지 번호
	@Value("#{target.joinchg.challengechg.chgno}")
	String getJoinchgChallengechgChgno();
	
	// 첼린지 제목
	@Value("#{target.joinchg.challengechg.chgtitle}")
	String getJoinchgChallengechgChgtitle();
	
	// 달성률 (Join Entity)
	@Value("#{target.joinchg.chgrate}")
	Float getChgrate();
	
	// 인증일
	Date getCcregdate();
	
}
