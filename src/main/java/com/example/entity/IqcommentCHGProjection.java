package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface IqcommentCHGProjection {

    Long GetIqcmtno();

    @Value("#{target.inquirychg.qno}")
    long getInquiryQno();

}
