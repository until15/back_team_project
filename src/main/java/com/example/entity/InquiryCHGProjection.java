package com.example.entity;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

public interface InquiryCHGProjection {

    Long getQno();

    String getQtitle();

    String getQcontent();

    LocalDate getQregdate();

    Long getCom();

    @Value("#{target.memberchg.memail}")
    String getMemail();

    @Value("#{target.memberchg.mid}")
    String getMid();

    @Value("#{target.memberchg.mrole}")
    String getMrole();

}
