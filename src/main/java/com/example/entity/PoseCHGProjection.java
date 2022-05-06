package com.example.entity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public interface PoseCHGProjection {

    Long getPno();

    String getPname();

    String getPpart();

    String getPcontent();

    Long getPlevel();

    Date getPregdate();

    @Value("#{target.memberchg.memail}")
    String getMemberchgMemail();

}
