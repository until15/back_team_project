package com.example.service;

import java.util.List;

import com.example.entity.CommentCHG;

import org.springframework.stereotype.Service;

@Service
public interface CommentService {

    // 댓글 등록
    public int commentInsert(CommentCHG comment);

    // 댓글 조회
    public CommentCHG selectOneComment(long cmtno);

    // 댓글 표시
    public List<CommentCHG> commentSelectList(Long bno);

    // 댓글 삭제
    public int deleteComment(long cmtno);

    // 좋아요
    public CommentCHG likeOne(long cmtno);

}
