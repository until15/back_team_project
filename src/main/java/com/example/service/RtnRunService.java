package com.example.service;

import org.springframework.stereotype.Service;

@Service
public interface RtnRunService {

    // 루틴 실행 등록
    public int RtnRunInsert(Long[] rtnno);
    
}
