package com.example.service;

import java.util.List;

import com.example.entity.RoutineCHG;

import org.springframework.stereotype.Service;

@Service
public interface RoutineService {

    // 루틴 등록
    public int RoutineInsertBatch(List<RoutineCHG> list);

    // 루틴 수정
    public int RoutineUpdateBatch(List<RoutineCHG> list);

    // 루틴 삭제
    public int RoutineDelete(Long[] rtnno);

    // 루틴 조회
    public List<RoutineCHG> RoutineSelectlist(String memail);
}
