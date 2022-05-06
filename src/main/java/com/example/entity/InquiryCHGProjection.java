package com.example.entity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public interface InquiryCHGProjection {

    Long getQno();

    String getQtitle();

    String getQcontent();

    Date getQregdate();

    @Value("#{target.memberchg.memail}")
    String getMemberchgMemail();

}
