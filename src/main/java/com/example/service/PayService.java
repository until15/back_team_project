package com.example.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public interface PayService {

    // 토큰 가져오기
    String getToken() throws IOException;
    
    // 결제 정보 조회
	int paySelectOne(String imp_uid, String access_token);
    
}
