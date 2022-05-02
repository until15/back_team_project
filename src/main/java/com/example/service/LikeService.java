package com.example.service;

import java.util.List;

import com.example.entity.LikeCHG;

import org.springframework.data.domain.Pageable;

public interface LikeService {
    
    // 좋아요 추가
    public int insertLike(LikeCHG like);

    // 중복
    public LikeCHG duplicateInsert(Long no, String memail);

    // 좋아요 삭제 
    public int deleteLike(long lno);

    // 좋아요 조회 
    public LikeCHG likeSelectOne(long lno);

    // 좋아요 목록
    public List<LikeCHG> likeSelectList(Pageable page, String memail);

}
