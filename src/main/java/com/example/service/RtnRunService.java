package com.example.service;

import java.util.List;

import com.example.entity.RtnRunCHG;

import org.springframework.stereotype.Service;

@Service
public interface RtnRunService {

    // 루틴 실행 등록
    public int RtnRunInsert(List<RtnRunCHG> rtnno);

    // 루틴 실행 수정
    public int RtnRunUpdate(List<RtnRunCHG> runno);

    // 루틴 실행 삭제
    public int RtnRunDelete(Long[] runno);

    // 루틴 실행 조회
    public List<RtnRunCHG> RtnRunSelectlist(long runseq);
    
}
