package com.example.service;

import java.util.List;

import com.example.entity.CmtLikeCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CmtLikeService {

    // 좋아요 추가
    public int insertLike(CmtLikeCHG cmtlike);

    // 중복
    public CmtLikeCHG duplicateInsert(Long no, String memail);

    // 좋아요 삭제
    public int deleteLike(long cmtlikeno);

    // 좋아요 조회
    public CmtLikeCHG likeSelectOne(long cmtlikeno);

    // 좋아요 목록
    public List<CmtLikeCHG> likeSelectList(Pageable page, String memail);

}
