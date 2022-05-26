package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface CommentCHGProjection {

    Long getCmtno();

    String getCmtcontent();

    @Value("#{target.memberchg.mid}")
    String getMid();
}
