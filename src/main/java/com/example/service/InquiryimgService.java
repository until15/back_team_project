package com.example.service;

import com.example.entity.InquiryimgCHG;

import org.springframework.stereotype.Service;

@Service
public interface InquiryimgService {

    // 이미지 등록
    public int insertQimg(InquiryimgCHG inquiryimg);

    // 이미지 1개 조회
    public InquiryimgCHG selectOneqimg(long qimgno);

    // 이미지 수정
    public int qimgUpdateOne(InquiryimgCHG inquiryimg);

    // 이미지 삭제
    public int deleteqimgOne(long qimgno);

}
