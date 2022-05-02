package com.example.service;

import java.util.List;

import com.example.entity.IqcommentCHG;

import org.springframework.stereotype.Service;

@Service
public interface IqcommentService {

    // 댓글 등록
    public int iqcommentInsert(IqcommentCHG iqcomment);

    // 댓글 조회
    public IqcommentCHG selectOneIqcomment(long iqcmtno);

    // 댓글 표시
    public List<IqcommentCHG> icommentSelectList(long qno);

    // 댓글 삭제
    public int deleteIqcomment(long iqcmtno);

}
