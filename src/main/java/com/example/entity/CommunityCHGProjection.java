package com.example.entity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public interface CommunityCHGProjection {
    Long getBno();

    String getBtitle();

    String getBcontent();

    Long getBhit();

    Date getBregdate();

    @Value("#{target.memberchg.memail}")
    String getMemail();

}