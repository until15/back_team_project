package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface RoutineCHGProjection {

    // 루틴 번호
    Long getRtnno();

    // 루틴 요일
    String getRtnday();

    // 운동 횟수
    Long getRtncnt();

    // 운동 세트
    Long getRtnset();

    // 루틴 이름
    String getRtnname();

    // 루틴 seq
    Long getRtnseq();

    // 자세 번호
    @Value("#{target.posechg.pno}")
    Long getPno();

    // 자세 이름
    @Value("#{target.posechg.pname}")
    String getPname();

    // 자세 부위
    @Value("#{target.posechg.ppart}")    
    String getPpart();

    // 자세 난이도
    @Value("#{target.posechg.plevel}") 
    Long getPlevel();

    // 회원 이메일
    @Value("#{target.memberchg.memail}")   
    String getMemail();
    
}
