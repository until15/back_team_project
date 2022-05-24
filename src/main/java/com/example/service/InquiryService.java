package com.example.service;

import java.util.List;

import com.example.entity.InquiryCHG;
import com.example.entity.InquiryCHGProjection;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface InquiryService {

    // 게시물 입력
    public long inquiryInsertOne(InquiryCHG inquiry);

    // 게시판 목록
    public List<InquiryCHG> selectInquiryList(Pageable page, String btitle);

    public List<InquiryCHGProjection> selectListInquiry(String memail, Pageable page, String btitle);

    // 게시물 1개 조회
    public InquiryCHG inquirySelectOne(long qno);

    // 게시물 수정
    public int inquiryUpdateOne(InquiryCHG inquiry);

    // 게시물 삭제
    public int inquiryDeleteOne(long qno);

    // 게시물 조회수 증가
    public int inquiryUpdateCom(long qno);

    public int inquiryTwoeCom(long qno);

}
