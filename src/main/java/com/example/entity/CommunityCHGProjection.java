package com.example.entity;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

public interface CommunityCHGProjection {
    Long getBno();

    String getBtitle();

    String getBcontent();

    Long getBhit();

    LocalDate getBregdate();

    @Value("#{target.memberchg.memail}")
    String getMemail();

    @Value("#{target.memberchg.mid}")
    String getMid();

}