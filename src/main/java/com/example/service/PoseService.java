package com.example.service;

import java.util.List;

import com.example.entity.PoseCHG;
import com.example.entity.VideoCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PoseService {

    // 자세 등록
    public int poseInsert(PoseCHG pose);

    // 자세 1개 조회
    public PoseCHG poseSelectOne(Long pno);

    // 자세 수정
    public int poseUpdate(PoseCHG pose);

    // 자세 삭제 (삭제 아닌 수정 pstep)
    public int poseDelete(PoseCHG pose);

    // 자세 검색어 개수
    public long poseCountSelect(String title);

    // 자세 목록
    public List<PoseCHG> poseSelectList(int step, Pageable page, String title);

    // 자세 동영상 등록
    public long poseVideoInsert(VideoCHG video);

    // 자세 동영상 수정
    public long poseVideoUpdate(VideoCHG video);

    // 자세 동영상 삭제
    public int poseVideoDelete(long vno);

    // 자세 동영상 불러오기
    public VideoCHG poseVideoSelectOne(long vno);
}
