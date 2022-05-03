package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class RtnRunServiceImpl implements RtnRunService{


    // 루틴 실행 등록
    @Override
    public int RtnRunInsert(Long[] rtnno) {
        try {
            

            return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
    
}
