package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface BimgCHGProjection {

    Long getBimgno();

    @Value("#{target.communitychg.bno}")
    long getCommunitychgBno();

}