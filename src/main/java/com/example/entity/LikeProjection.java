package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface LikeProjection {

    // 좋아요 번호 
    Long getLno();
    
    @Value("#{target.challengechg.chgno}")
	String getChgno();

    @Value("#{target.challengechg.chgtitle}")
	String getChgtitle();

    @Value("#{target.challengechg.chgcnt}")
	String getChgcnt();

    @Value("#{target.challengechg.chgfee}")
	String getChgfee();

    @Value("#{target.challengechg.chglevel}")
	String getChglevel();

    @Value("#{target.challengechg.chglike}")
	String getChglike();

    // 회원 아이디 
    @Value("#{target.memberchg.memail}")
	String getMemberchgMemail();
}
