package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface InquiryimgCHGProjection {

    Long getQimgno();

    @Value("#{target.communitychg.bno}")
    long getCommunitychgBno();

}
