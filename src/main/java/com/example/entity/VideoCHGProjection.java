package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface VideoCHGProjection {

    Long getVno();

    @Value("#{target.posechg.pno}")
    long getPosechgPno();
}
