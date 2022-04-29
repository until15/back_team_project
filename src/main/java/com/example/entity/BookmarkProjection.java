package com.example.entity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public interface BookmarkProjection {
    
    // 북마크 번호
    Long getBmkno();

    // 북마크 여부
    Integer getBmkstate();

    // 북마크 일자
    Date getBmkregdate();

    // 첼린지 번호
	@Value("#{target.challengechg.chgno}")
	Long getChallengeCHGChgno();
    
    // 참가자 이메일
	@Value("#{target.memberchg.memail}")
	String getMemberCHGMemail();
}
