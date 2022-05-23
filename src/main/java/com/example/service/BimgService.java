package com.example.service;

import java.util.List;

import com.example.entity.BimgCHG;
import com.example.entity.BimgCHGProjection;

import org.springframework.stereotype.Service;

@Service
public interface BimgService {

    // 이미지 등록
    public int insertBimg(BimgCHG bimg);

    // 이미지 1개 조회
    public BimgCHG selectOneimage(long bimgno);

    // 이미지 목록 보이기
    public List<BimgCHGProjection> selectimageList(long bimgno);

    // 이미지 수정
    public int bimgUpdateOne(BimgCHG bimg);

    // 이미지 삭제
    public int deleteBimgOne(long bimgno);

    public int insertBatchBimg(List<BimgCHG> list);

}
