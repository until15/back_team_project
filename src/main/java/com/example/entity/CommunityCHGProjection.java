package com.example.entity;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

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