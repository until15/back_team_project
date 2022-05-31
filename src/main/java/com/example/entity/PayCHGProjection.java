package com.example.entity;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

public interface PayCHGProjection {

    // 고유 ID
    String getImpuid();

    // 결제 ID
    String getMerchantuid();

    // 결제 금액
    int getPprice();

    // 환불 가능 금액
    int getCancelprice();

    // 결제일
    LocalDate getPregdate();

    // 챌린지 참여 번호
    @Value("#{target.joinchg.jno}")
    Long getJno();

    // 참여한 챌린지 번호
    @Value("#{target.joinchg.challengechg.chgno}")
    Long getChgno();

    // 유저 이메일
    @Value("#{target.joinchg.memberchg.memail}")
    String getMemail();

    // 참여한 챌린지 이름
    @Value("#{target.joinchg.challengechg.chgtitle}")
    String getChgtitle();

    // 참여한 챌린지 달성률
    @Value("#{target.joinchg.chgrate}")
    float getChgrate();

}
