package com.example.service;

import java.util.List;

import com.example.entity.LikeCHG;
import com.example.entity.LikeProjection;
import com.example.repository.LikeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired LikeRepository lRepository;

    // 좋아요 추가
    @Override
    public int insertLike(LikeCHG like) {
        try {
            lRepository.save(like);
            return 1;
        }
        catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 좋아요 삭제
    @Override
    public int deleteLike(long lno) {
        try {   
            lRepository.deleteById(lno);
            return 1;
        }
        catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 좋아요 조회
    @Override
    public LikeCHG likeSelectOne(long lno) {
        try {
            return lRepository.findById(lno).orElse(null);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 좋아요 목록 (페이지네이션)
    @Override
    public List<LikeProjection> likeSelectList(Pageable page, String memail) {
        try {
            List<LikeProjection> list = lRepository.findByMemberchg_memailOrderByLnoDesc(memail, page);
            return list;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 중복방지
    @Override
    public LikeCHG duplicateInsert(Long no, String memail) {
        try {
            return lRepository.findByChallengechg_chgnoAndMemberchg_memail(no, memail);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
     
    }


}
