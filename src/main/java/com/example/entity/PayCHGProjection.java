package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface PayCHGProjection {

    // 결제 금액
    Long getPprice();

    // 챌린지 참여 번호
    @Value("#{target.joinchg.jno}")
    Long getJno();

    // 참여한 챌린지 번호
    @Value("#{target.joinchg.chgno}")
    Long getChgno();

    // 유저 이메일
    @Value("#{target.joinchg.memberchg.memail}")
    String getMemail();

    // 참여한 챌린지 이름
    @Value("#{target.joinchg.challengechg.chgtitle}")
    String getChgtitle();
    
}
