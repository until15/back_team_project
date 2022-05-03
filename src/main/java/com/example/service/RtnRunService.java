package com.example.service;

import java.util.List;

import com.example.entity.RtnRunCHG;

import org.springframework.stereotype.Service;

@Service
public interface RtnRunService {

    // 루틴 실행 등록
    public int RtnRunInsert(List<RtnRunCHG> runno);
    
}
