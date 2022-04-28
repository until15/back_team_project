package com.example.service;

import java.util.List;

import com.example.entity.CommunityCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CommuniryService {

    // 게시물 입력
    public int boardInsertOne(CommunityCHG community);

    // 게시판 목록
    public List<CommunityCHG> selectBoardList(Pageable page, String btitle);

    // 게시물 1개 조회
    public CommunityCHG boardSelectOne(long bno);

    // 게시물 수정
    public int boardUpdateOne(CommunityCHG community);

    // 게시물 삭제
    public int boarddeleteOne(long bno);

    // 게시물 조회수 증가
    public int boardUpdateHit(long bno);

    // 게시물 조회수 증가2
    public CommunityCHG boardUpdateHit1(long bno);
}
